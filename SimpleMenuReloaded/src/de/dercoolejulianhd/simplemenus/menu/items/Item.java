package de.dercoolejulianhd.simplemenus.menu.items;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class Item implements ConfigurationSerializable {

    private final int slot;
    private final Material material;
    private final int id;
    private final ItemMeta itemMeta;
    private final ItemStack itemStack;

    public Item(int slot, Material material, int id, String displayName) {
        this.slot = slot;
        this.material = material;
        this.id = id;
        this.itemStack = new ItemStack(material, 1, (byte) id);
        this.itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
    }

    public Item(int slot, Material material, int id, ItemMeta meta) {
        this.slot = slot;
        this.material = material;
        this.id = id;
        this.itemStack = new ItemStack(material, 1, (byte) id);
        this.itemMeta = meta;
        this.itemStack.setItemMeta(meta);
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("slot", this.slot);
        data.put("material", this.material.name());
        data.put("id", this.id);
        data.put("meta", this.itemMeta);
        return data;
    }

    public static Item deserialize(Map<String, Object> args) {
        int slot = (int) args.get("slot");
        String materialName = ((String) args.get("material")).toUpperCase();
        Material material = Material.getMaterial(materialName);
        int id = (int) args.get("id");
        ItemMeta meta = (ItemMeta) args.get("meta");
        return new Item(slot, material, id, meta);
    }

    public int getSlot() {
        return slot - 1;
    }

    public Material getMaterial() {
        return material;
    }

    public int getId() {
        return id;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public static Item itemStackToItemBySlot(int slot, ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return new Item(slot, itemStack.getType(), 0, meta);
    }
}
