package me.ram.kungfu.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.ram.kungfu.KungFu;

public class PAPIHook extends EZPlaceholderHook {

    public PAPIHook(Plugin plugin) {
        super(plugin, "kungfu");
    }
    
    public String onPlaceholderRequest(Player player, String arg) {
        if (player != null && arg.equalsIgnoreCase("coin")) {
        	return String.valueOf(KungFu.getInstance().getPlayerData().getPlayerCoin(player.getName()));
        }
    	return "null";
    }
    
}
