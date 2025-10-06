package org.astarisucu.aceLib.Utiles;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Api {
    private static final String BASE_URL = "http://localhost:8000"; // APIサーバーURL
    private static final String OPENAPI_URL = BASE_URL + "/openapi.json";
    private static Map<String, ApiDefinition> apiMap = null;
    private static final Gson gson = new Gson();
    public static void loadOpenAPI() throws IOException {
        if (apiMap != null) return;

        apiMap = new HashMap<>();
        String json = readURL();
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonObject paths = root.getAsJsonObject("paths");

        for (String path : paths.keySet()) {
            JsonObject post = paths.getAsJsonObject(path).getAsJsonObject("post");
            if (post == null) continue;

            JsonObject reqBody = post.getAsJsonObject("requestBody");
            if (reqBody == null) continue;

            JsonObject schema = reqBody
                    .getAsJsonObject("content")
                    .getAsJsonObject("application/json")
                    .getAsJsonObject("schema");

            List<String> required = new ArrayList<>();
            if (schema.has("required")) {
                for (JsonElement el : schema.getAsJsonArray("required")) {
                    required.add(el.getAsString());
                }
            }

            JsonObject props = schema.getAsJsonObject("properties");
            Map<String, String> propMap = new LinkedHashMap<>();
            for (String key : props.keySet()) {
                propMap.put(key, props.getAsJsonObject(key).get("type").getAsString());
            }

            apiMap.put(path.replaceFirst("^/", ""), new ApiDefinition(path, required, propMap));
        }
    }

    public static Map<String, Object> RequestAPI(String endpoint, Object... args) throws Exception {
        if (apiMap == null) loadOpenAPI();
        ApiDefinition def = apiMap.get(endpoint);
        if (def == null) throw new IllegalArgumentException("Unknown endpoint: " + endpoint);

        int expected = def.fieldTypes.size();
        if (args.length != expected) {
            throw new IllegalArgumentException(
                    String.format("Endpoint '%s' expects %d args but got %d",
                            endpoint, expected, args.length));
        }

        // 引数をフィールド名と対応付け
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        int i = 0;
        for (String field : def.fieldTypes.keySet()) {
            jsonMap.put(field, args[i++]);
        }

        // JSON作成
        String jsonBody = gson.toJson(jsonMap);

        // POST送信
        URL url = new URL(BASE_URL + "/" + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        conn.disconnect();

        // JSON→Map変換
        try {
            return gson.fromJson(response, Map.class);
        } catch (JsonSyntaxException e) {
            // JSONでない場合（単純な文字列や成功メッセージなど）はMapで包む
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("response", response);
            fallback.put("status", status);
            return fallback;
        }
    }

    private static class ApiDefinition {
        String path;
        List<String> requiredFields;
        Map<String, String> fieldTypes;

        ApiDefinition(String path, List<String> required, Map<String, String> fields) {
            this.path = path;
            this.requiredFields = required;
            this.fieldTypes = fields;
        }
    }

    private static String readURL() throws IOException {
        try (InputStream in = new URL(Api.OPENAPI_URL).openStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
