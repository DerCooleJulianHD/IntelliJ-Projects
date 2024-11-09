package de.dercoolejulianhd.simplemenus;

import com.avaje.ebean.validation.NotNull;
import de.dercoolejulianhd.simplemenus.commands.MenuCreationCMD;
import de.dercoolejulianhd.simplemenus.commands.OpenMenuCMD;
import de.dercoolejulianhd.simplemenus.handlers.ConnectionListener;
import de.dercoolejulianhd.simplemenus.menu.InteractiveMenu;
import de.dercoolejulianhd.simplemenus.menu.Menu;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import de.dercoolejulianhd.simplemenus.menu.settings.CraftMenuSettings;
import de.dercoolejulianhd.simplemenus.utility.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Main extends JavaPlugin implements SimpleMenuReloaded {

    private static Main instance;
    private File sourceFolder;

    private List<Menu> loadedMenus;

    @Override
    public void onLoad() {
        instance = this;
        this.sourceFolder = new File(this.getDataFolder(), "menus");
        this.loadedMenus = new ArrayList<>();
        a();
    }

    @Override
    public void onEnable() {
        this.loadMenus();
        new MenuCreationCMD(this);
        new OpenMenuCMD(this);
        new ConnectionListener(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @NotNull
    public Menu loadMenu(String name) {
        Configuration menuConfig = new Configuration(this, this.getSourceFolder(), name + ".yml");
        if (!menuConfig.exists()) return null;
        if (!menuConfig.isLoaded()) menuConfig.load();
        return new InteractiveMenu(menuConfig);
    }

    public void loadMenus() {
        File[] list = this.getSourceFolder().listFiles();
        if (list == null || list.length == 0) return;
        List<File> files = Arrays.stream(list).toList();
        files.forEach(file -> {
            String name = file.getName().replaceAll(".yml", "");
            try {
                Menu menu = this.loadMenu(name);
                this.getLoadedMenus().add(menu);
                getLogger().log(Level.INFO, "Menu " + menu.getName() + " loaded successfully!");
            } catch (Exception ex) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + ChatColor.RED + "could not load Menu " + name + "!, " + ex);
                throw new RuntimeException(ex);
            }
        });
    }

    public void openMenu(Player player, String name) {
        Menu menu = this.getMenuLoaded(name);
        if (menu == null) menu = this.loadMenu(name);
        if (menu == null) {
            this.getLogger().log(Level.SEVERE, "cannot open menu " + name + "while not exist!");
            return;
        }
        menu.open(player, menu.getOpenSound());
    }

    public Menu getMenuLoaded(String name) {
        for (Menu menu : this.getLoadedMenus()) {
            if (menu.getName().equalsIgnoreCase(name)) {
                return menu;
            }
        }
        return null;
    }

    private void a() {
        ConfigurationSerialization.registerClass(CraftMenuSettings.class);
        ConfigurationSerialization.registerClass(Item.class);
    }

    public static Main getInstance() {
        return instance;
    }

    public File getSourceFolder() {
        return sourceFolder;
    }

    public List<Menu> getLoadedMenus() {
        return loadedMenus;
    }

    @Override
    public Main getPlugin() {
        return this;
    }

    public static String getPrefix() {
        return "§8[§aSimpleMenusReloaded§8] §r";
    }
}
