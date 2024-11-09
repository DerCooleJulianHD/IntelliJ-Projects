package de.dercoolejulianhd.simplemenus.handlers;

import de.dercoolejulianhd.simplemenus.Main;
import de.dercoolejulianhd.simplemenus.menu.Menu;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import de.dercoolejulianhd.simplemenus.menu.settings.MenuSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public record ConnectionListener(Plugin plugin) implements Listener {

    public ConnectionListener(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Menu menu : Main.getInstance().getLoadedMenus()) {
            MenuSettings settings = menu.getSettings();
            if (settings.isOpenItemActive()) {
                PlayerInventory inv = player.getInventory();
                Item o = menu.getOpenItem();
                ItemStack itemStack = o.toItemStack();
                if (inv.contains(itemStack)) return;
                else inv.setItem(o.getSlot(), itemStack);
            }
        }
    }
}
