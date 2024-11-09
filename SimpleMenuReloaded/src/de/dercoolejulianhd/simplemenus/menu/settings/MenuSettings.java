package de.dercoolejulianhd.simplemenus.menu.settings;

import de.dercoolejulianhd.simplemenus.menu.items.Item;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface MenuSettings extends ConfigurationSerializable {

    String getMenuName();
    int getRows();
    String getOpenSound();
    Item getOpenItem();
    boolean isCanMoveItems();
    boolean isOpenItemActive();

}
