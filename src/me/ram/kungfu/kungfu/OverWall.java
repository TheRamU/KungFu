package me.ram.kungfu.kungfu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class OverWall implements Listener {
	
	private Map<UUID, Long> sneakinterval = new HashMap<UUID, Long>();
	private Map<UUID, BukkitTask> players = new HashMap<UUID, BukkitTask>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onToggleSneak(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
		if (!player.isSneaking()) {
			return;
		}
		KungFuType type = KungFuType.OverWall;
		if (PlayerStorage.isDisable(player, type)) {
			return;
		}
		int level = PlayerStorage.getLevel(player, type);
		if (level < 1) {
			return;
		}
		if (level > KungFu.getInstance().getFullLevel().get(type)) {
			level = KungFu.getInstance().getFullLevel().get(type);
		}
		if ((System.currentTimeMillis() - sneakinterval.getOrDefault(player.getUniqueId(), (long) 0)) < 500) {
			Location location = player.getLocation();
			Block block = player.getTargetBlock((Set)null, 1);
			Block block2 = location.clone().add(0, -1, 0).getBlock();
			if (block2 != null && !block2.getType().equals(Material.AIR) && block != null && !block.getType().equals(Material.AIR) && block.getY() > location.getBlockY() && block.getX() != location.getX() && block.getZ() != location.getZ()) {
				overWall(player, (int) Config.kungfu_level_value.get(type).get(level));
			}
		}else {
			sneakinterval.put(player.getUniqueId(), System.currentTimeMillis());
		}
	}
	
	@EventHandler
	public void onChangedWorld(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		if (players.containsKey(player.getUniqueId())) {
			players.get(player.getUniqueId()).cancel();
			players.remove(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player player = e.getPlayer();
		if (players.containsKey(player.getUniqueId())) {
			players.get(player.getUniqueId()).cancel();
			players.remove(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (players.containsKey(player.getUniqueId())) {
			players.get(player.getUniqueId()).cancel();
			players.remove(player.getUniqueId());
		}
	}
	
	private void overWall(Player player, int a) {
		players.put(player.getUniqueId(), new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if (i >= a) {
					cancel();
					players.remove(player.getUniqueId());
				}
				Location location = player.getLocation();
				Block block = player.getTargetBlock((Set)null, 1);
				if (player.isOnline() && !player.isDead() && block != null && !block.getType().equals(Material.AIR) && block.getX() != location.getX() && block.getZ() != location.getZ()) {
					player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.65));
				    i++;
				}else {
					cancel();
					players.remove(player.getUniqueId());
				}
			}
		}.runTaskTimer(KungFu.getInstance(), 0L, 5L));
	}
}
