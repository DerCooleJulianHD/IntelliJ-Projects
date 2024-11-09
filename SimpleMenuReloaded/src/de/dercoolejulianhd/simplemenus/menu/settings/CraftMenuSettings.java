package de.dercoolejulianhd.simplemenus.menu.settings;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CraftMenuSettings implements MenuSettings {

    private final String name;
    private final int rows;
    private final String openSound;
    private final Item openItem;
    private final boolean canMoveItems;
    private final boolean openItemActive;

    public CraftMenuSettings(String name, int rows, String openSound, Item openItem, boolean canMoveItems, boolean openItemActive) {
        this.name = name;
        this.rows = rows;
        this.openSound = openSound;
        this.openItem = openItem;
        this.canMoveItems = canMoveItems;
        this.openItemActive = openItemActive;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", this.name);
        data.put("rows", this.rows);
        data.put("open-sound", this.openSound.toUpperCase());
        data.put("allow-item-move", this.canMoveItems);
        data.put("open-item.get-on-join", this.openItemActive);
        data.put("open-item.item", this.openItem);
        return data;
    }

    public static MenuSettings deserialize(Map<String, Object> args) {
        String name = (String) args.get("name");
        int rows = (int) args.get("rows");
        String openSound = ((String) args.get("open-sound")).toUpperCase();
        Item openItem = (Item) args.get("open-item.item");
        boolean canMoveItems = (boolean) args.get("allow-item-move");
        boolean openItemActive = (boolean) args.get("open-item.get-on-join");
        return new CraftMenuSettings(name, rows, openSound, openItem, canMoveItems, openItemActive);
    }

    public String getMenuName() {
        return this.name;
    }

    public int getRows() {
        return rows;
    }

    public String getOpenSound() {
        return openSound;
    }

    public Item getOpenItem() {
        return openItem;
    }

    public boolean isCanMoveItems() {
        return canMoveItems;
    }

    public boolean isOpenItemActive() {
        return openItemActive;
    }
}
