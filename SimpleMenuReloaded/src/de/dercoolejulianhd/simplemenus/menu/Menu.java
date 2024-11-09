package de.dercoolejulianhd.simplemenus.menu;

import de.dercoolejulianhd.simplemenus.menu.items.Item;
import de.dercoolejulianhd.simplemenus.menu.settings.MenuSettings;
import de.dercoolejulianhd.simplemenus.utility.Configuration;
import org.apache.logging.log4j.Logger;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.Set;

public interface Menu {

    Configuration getConfiguration();

    String getName();
    Set<Player> getViewers();
    Inventory getInventoryView();
    Map<Integer, Item> getItemsSet();
    Item getItemSet(int slot);
    Item getOpenItem();
    Sound getOpenSound();
    Logger getLogger();
    MenuSettings getSettings();

    boolean open(Player player, Sound sound);
    boolean isViewer(Player player);
    boolean hasOpen(Player player);
    boolean canMoveItems();

    void close(Player player);
    void close();
    void setCanMoveItems(boolean b);
    void refresh();
    void fill();
    void setItemSet(Map<Integer, Item> value);
    void addViewer(Player player);
    void removeViewer(Player player);
    void clear();
    void setViewers(Set<Player> value);
}
