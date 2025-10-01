# AceLib
## InvUI
```java
 ui = new InvUi(id,plugin,SlotSize,titleText);
```

### addButton
addButton(SlotNumber,DisplayName,MaterialID,method);<br>
add Button(Map<Integer,Button>)

### deleteButton
deleteButton(SlotNumber);<br>
delete Button,in this number slot.

### getButton
 getButton();<br>
 get Buttons(Map<Integer,Button(ItemStack,Consumer<Player>)>)

### build
build();
get this Button,Cast to Inventory

### open
open(Player);<br>
open this InventoryUI.<br>
Internally, it uses the build method.

## Example
```java
testUI = new InvUI("test_ui", this, 27, "テストメニュー");
testUI.addButton(11, "ダイヤをもらう", "DIAMOND", player -> {
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        });
testUI.addButton(15, "閉じる", "BARRIER", player -> {
        player.closeInventory();
        });

testUI.open(player);
```
![img.png](img.png)