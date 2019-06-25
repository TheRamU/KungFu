package me.ram.kungfu.kungfu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class WaterWalk implements Listener {
	
	private Map<UUID, Long> moveinterval = new HashMap<UUID, Long>();
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.isCancelled() || !player.isSprinting()) {
        	return;
        }
        if ((System.currentTimeMillis() - moveinterval.getOrDefault(player.getUniqueId(), (long) 0)) < 100) {
        	return;
        }
        moveinterval.put(player.getUniqueId(), System.currentTimeMillis());
		Block block = player.getLocation().clone().getBlock();
		Block block2 = player.getLocation().clone().add(0, -1, 0).getBlock();
		Block block3 = player.getLocation().clone().add(0, 1, 0).getBlock();
		if ((player.getGameMode() == GameMode.SPECTATOR || (block != null && block.getType().name().contains("WATER")) || (block2 != null && block2.getType().name().contains("WATER"))) && (block3 == null || !block3.getType().name().contains("WATER"))) {
			KungFuType type = KungFuType.WaterWalk;
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
			player.setVelocity(player.getLocation().getDirection().multiply(0.3).multiply(((int) Config.kungfu_level_value.get(type).get(level))).setY(0.2));
		}
    }
}
