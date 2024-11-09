package de.dercoolejulianhd.simplemenus.commands;

import de.dercoolejulianhd.simplemenus.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public record OpenMenuCMD(Plugin plugin) implements CommandExecutor {

    public OpenMenuCMD(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginCommand("openmenu").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "Usage: /openmenu <name>");
            return false;
        }

        Main.getInstance().openMenu(player, args[0]);
        return true;
    }
}
