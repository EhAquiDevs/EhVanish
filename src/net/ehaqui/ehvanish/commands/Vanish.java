package net.ehaqui.ehvanish.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.ehaqui.ehcore.utils.Utils;
import net.ehaqui.ehvanish.Main;

public class Vanish implements CommandExecutor {
    private Main plugin;

    public Vanish (Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.getPrefix() + ChatColor.DARK_RED + "Esse comando só pode ser executado dentro do servidor!");
            return false;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("ehcore.vanish.use")) {
            p.sendMessage(Utils.getPrefix() + ChatColor.DARK_RED + "Você não tem permissão para executar esse comando!");
            return false;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(p)) {
                continue;
            }

            if (!player.hasPermission("ehcore.vanish.bypass")) {
                if (!p.hasMetadata("vanish")) {
                    player.hidePlayer(this.plugin, p);
                } else {
                    player.showPlayer(this.plugin, p);
                }
            }
        }

        if (p.hasMetadata("vanish")) {
            p.sendMessage(Utils.getPrefix() + ChatColor.DARK_AQUA + "Você está vísivel para outros jogadores.");
            p.removeMetadata("vanish", this.plugin);
            Main.vanished.add(p.getUniqueId());
        } else {
            p.sendMessage(Utils.getPrefix() + ChatColor.DARK_AQUA + "Você está invísivel para outros jogadores.");
            p.setMetadata("vanish", new FixedMetadataValue(this.plugin, true));
            Main.vanished.remove(p.getUniqueId());
        }

        return true;
    }
}
