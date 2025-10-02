package org.astarisucu.aceLib.Event;

import org.astarisucu.aceLib.Utiles.UIRegisterData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * サブプラグインがUIを登録したいときに発火するイベント
 */
public class UIRegisterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final UIRegisterData data;

    public UIRegisterEvent(UIRegisterData data) {
        this.data = data;
    }

    public UIRegisterData getData() { return data; }

    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}