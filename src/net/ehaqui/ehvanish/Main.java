package net.ehaqui.ehvanish;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import net.ehaqui.ehcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.ehaqui.ehvanish.commands.Vanish;

public class Main extends JavaPlugin implements Listener {
    public static List<UUID> vanished = new ArrayList<>();

    @Override
    public void onLoad() {
        this.getLogger().log(Level.FINE, "Carregado");
    }

    @Override
    public void onEnable () {
        this.getCommand("vanish").setExecutor(new Vanish(this));

        PluginManager manager = Bukkit.getServer().getPluginManager();

        manager.registerEvents(this, this);
    }

    @Override
    public void onDisable () {
        this.getLogger().log(Level.FINE, "Desativado");
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (vanished.contains(p.getUniqueId()))
            p.setMetadata("vanish", new FixedMetadataValue(this, true));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("ehcore.vanish.bypass") && player.hasMetadata("vanish"))
                p.hidePlayer(this, player);
        }

        if (p.hasMetadata("vanish")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(this, p);
            }

            p.sendMessage(Utils.getPrefix() +
                    ChatColor.DARK_AQUA +
                    "Você entrou no servidor com o modo invisível ativado. Para desativar, use " +
                    ChatColor.AQUA +
                    "/vanish");
        }
    }
}
