package me.ram.kungfu.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;

public class PlayerStorage {
	
	private static Map<UUID, Map<String, Integer>> level = new HashMap<UUID, Map<String, Integer>>();
	private static Map<UUID, List<String>> disable = new HashMap<UUID, List<String>>();
	
	public static Map<String, Integer> getLevel(Player player) {
		return level.getOrDefault(player.getUniqueId(), new HashMap<String, Integer>());
	}
	
	public static Integer getLevel(Player player, KungFuType type) {
		return level.getOrDefault(player.getUniqueId(), new HashMap<String, Integer>()).getOrDefault(type.toString(), 0);
	}
	
	public static void setLevel(Player player, Map<String, Integer> level) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String kungfu : level.keySet()) {
			map.put(kungfu, level.get(kungfu));
		}
		PlayerStorage.level.put(player.getUniqueId(), map);
	}
	
	public static boolean isDisable(Player player, KungFuType type) {
		return disable.getOrDefault(player.getUniqueId(), new ArrayList<String>()).contains(type.toString());
	}
	
	public static List<String> getDisable(Player player) {
		return disable.getOrDefault(player.getUniqueId(), new ArrayList<String>());
	}
	
	public static void setDisable(Player player, List<String> disable) {
		PlayerStorage.disable.put(player.getUniqueId(), disable);
	}
	
	public static void update() {
		level = new HashMap<UUID, Map<String, Integer>>();
		disable = new HashMap<UUID, List<String>>();
        for (Player player : Bukkit.getOnlinePlayers()) {
    		new BukkitRunnable() {
    			@Override
    			public void run() {
    				if (player.isOnline()) {
    					PlayerStorage.setLevel(player, KungFu.getInstance().getPlayerData().getPlayerLevel(player.getName()));
    					PlayerStorage.setDisable(player, KungFu.getInstance().getPlayerData().getPlayerDisable(player.getName()));
    				}
    			}
    		}.runTaskLater(KungFu.getInstance(), 1L);
        }
	}
}
