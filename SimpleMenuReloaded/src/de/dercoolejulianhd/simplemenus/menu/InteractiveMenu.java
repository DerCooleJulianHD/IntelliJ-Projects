package de.dercoolejulianhd.simplemenus.menu;

import de.dercoolejulianhd.simplemenus.Main;
import de.dercoolejulianhd.simplemenus.handlers.OpenItemListener;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import de.dercoolejulianhd.simplemenus.menu.settings.MenuSettings;
import de.dercoolejulianhd.simplemenus.utility.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class InteractiveMenu implements Menu, Listener {

    private static final Logger logger = LogManager.getLogger(InteractiveMenu.class);

    private final Configuration configuration;

    private final MenuSettings settings;
    private boolean canItemMove;
    private Set<Player> viewers;

    private final Inventory inventory;

    private Map<Integer, Item> items;

    public InteractiveMenu(Configuration configuration) {
        this.configuration = configuration;
        this.viewers = new HashSet<>();
        this.settings = (MenuSettings) configuration.getFileConfiguration().get("menu");
        this.canItemMove = settings.isCanMoveItems();
        this.inventory = Bukkit.createInventory(null, 9*settings.getRows(), this.getName());
        this.items = this.loadItems();
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        new OpenItemListener(Main.getInstance(), this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public String getName() {
        return this.settings.getMenuName();
    }

    @Override
    public Set<Player> getViewers() {
        return this.viewers;
    }

    @Override
    public Inventory getInventoryView() {
        return this.inventory;
    }

    @Override
    public Map<Integer, Item> getItemsSet() {
        return this.items;
    }

    @Override
    public Item getItemSet(int slot) {
        return this.items.get(slot);
    }

    @Override
    public Item getOpenItem() {
        return settings.getOpenItem();
    }

    @Override
    public Sound getOpenSound() {
        if (settings.getOpenSound().equalsIgnoreCase("NONE")) return null;
        return Sound.valueOf(settings.getOpenSound());
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean open(Player player, Sound sound) {
        boolean b = this.hasOpen(player);
        if (b)return true;
        if (this.inventory.getContents().length != 0) this.refresh();
        else this.fill();
        player.openInventory(this.getInventoryView());
        if (sound != null) player.playSound(player.getLocation(), sound, 3, 1);
        this.addViewer(player);
        return true;
    }

    @Override
    public boolean isViewer(Player player) {
        return this.viewers.contains(player);
    }

    @Override
    public boolean hasOpen(Player player) {
        return this.isViewer(player);
    }

    @Override
    public boolean canMoveItems() {
        return this.canItemMove;
    }

    @Override
    public void close(Player player) {
        boolean b = this.hasOpen(player);
        if (!b) return;
        player.closeInventory();
        this.removeViewer(player);
    }

    @Override
    public void close() {
        this.getViewers().forEach(this::close);
    }

    @Override
    public void setCanMoveItems(boolean b) {
        this.canItemMove = b;
    }

    @Override
    public void refresh() {
        this.clear();
        this.fill();
    }

    @Override
    public void fill() {
        this.items.forEach((slot, item) -> {
            inventory.setItem(slot, item.toItemStack());
        });
    }

    @Override
    public void setItemSet(Map<Integer, Item> value) {
        this.items = value;
    }

    @Override
    public void addViewer(Player player) {
        this.getViewers().add(player);
    }

    @Override
    public void removeViewer(Player player) {
        this.getViewers().remove(player);
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void setViewers(Set<Player> value) {
        this.viewers = value;
    }

    public void saveItems() {
        FileConfiguration fc = this.configuration.getFileConfiguration();
        List<Item> list = new ArrayList<>(this.items.values());
        this.configuration.set("items", list);
        this.configuration.save();
    }

    public static void saveItems(Menu menu, List<Item> items) {
        Configuration c = menu.getConfiguration();
        FileConfiguration fc = c.getFileConfiguration();
        fc.set("items", items);
        c.save();
    }

    public Map<Integer, Item> loadItems() {
        Map<Integer, Item> result = new HashMap<>();
        FileConfiguration fc = this.configuration.getFileConfiguration();
        if (fc.isSet("items")) {
            List<?> list = fc.getList("items");
            for (Object o : list) {
                if (!(o instanceof Item i)) continue;
                result.put(i.getSlot(), i);
            }
        }
        return result;
    }

    @Override
    public MenuSettings getSettings() {
        return settings;
    }

    public void handleInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(this.getInventoryView())) return;
        if (this.canMoveItems()) {
            event.setCancelled(false);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        this.removeViewer(((Player) event.getPlayer()));
    }

}
