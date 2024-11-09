package de.dercoolejulianhd.simplemenus.commands;

import de.dercoolejulianhd.simplemenus.Main;
import de.dercoolejulianhd.simplemenus.menu.InteractiveMenu;
import de.dercoolejulianhd.simplemenus.menu.Menu;
import de.dercoolejulianhd.simplemenus.menu.items.Item;
import de.dercoolejulianhd.simplemenus.menu.settings.CraftMenuSettings;
import de.dercoolejulianhd.simplemenus.utility.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MenuCreationCMD implements CommandExecutor, Listener {

    private final Plugin plugin;
    private final List<Item> items;

    private Menu menu;
    private Inventory inventory;

    public MenuCreationCMD(Plugin plugin) {
        this.plugin = plugin;
        this.items = new ArrayList<>();
        Bukkit.getPluginCommand("createmenu").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "Usage: /createmenu <name> <size>");
            return false;
        }

        Configuration configuration = new Configuration(plugin, Main.getInstance().getSourceFolder(), args[0] + ".yml");
        if (configuration.exists()) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "This Menu already exists!");
            return true;
        }

        configuration.createConfigFiles(false);
        configuration.load();
        try {
            int rows = Integer.parseInt(args[1]);
            if (rows < 1 || rows > 6) {
                sender.sendMessage(Main.getPrefix() + ChatColor.RED + "<size> must be a number from  1 to 6!");
                return false;
            }
            configuration.getFileConfiguration().set("menu", new CraftMenuSettings(
                    args[0],
                    rows,
                    Sound.CLICK.name(),
                    new Item(1, Material.BOOK, 0, args[0]),
                    false,
                    true
            ));
            configuration.save();
            this.menu = Main.getInstance().loadMenu(args[0]);

            if (sender instanceof Player player) {
                this.inventory = Bukkit.createInventory(null, 9*rows, args[0]);
                player.openInventory(this.inventory);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 1);
                Bukkit.getPluginManager().registerEvents(this, plugin);
            }

        } catch (NumberFormatException exception) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "Invalid Condition:" + exception.getMessage());
            throw new RuntimeException(exception);
        }
        return true;
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (!inv.equals(this.getInventory())){
            return;
        }

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR){
                continue;
            }

            items.add(Item.itemStackToItemBySlot(i + 1, item));
        }

        InteractiveMenu.saveItems(menu, items);
        HandlerList.unregisterAll(this);
        Player player = ((Player) event.getPlayer());
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 1);
        player.sendMessage(Main.getPrefix() + ChatColor.GREEN + this.menu.getName() + " created successfully!");
    }


    public Inventory getInventory() {
        return inventory;
    }
}
