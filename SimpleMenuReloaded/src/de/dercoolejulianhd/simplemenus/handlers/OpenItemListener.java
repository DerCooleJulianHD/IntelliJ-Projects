package de.dercoolejulianhd.simplemenus.handlers;

import de.dercoolejulianhd.simplemenus.Main;
import de.dercoolejulianhd.simplemenus.menu.Menu;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public record OpenItemListener(Plugin plugin, Menu menu) implements Listener {

    public OpenItemListener(Plugin plugin, Menu menu) {
        this.plugin = plugin;
        this.menu = menu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handleClickItem(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == null) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK: {
                if (!event.hasItem()) return;
                Item openItem = menu.getOpenItem();
                if (openItem == null) return;
                if (!event.getItem().equals(openItem.toItemStack())) return;
                menu.open(player, menu.getOpenSound());
            }
        }
    }
}
