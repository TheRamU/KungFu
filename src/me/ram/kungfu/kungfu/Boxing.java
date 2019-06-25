package me.ram.kungfu.kungfu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class Boxing implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getDamager();
		KungFuType type = KungFuType.Boxing;
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
		if (KungFu.getInstance().isItemInHand(player, Material.AIR)) {
			double multiple = (Double.valueOf((int) Config.kungfu_level_value.get(type).get(level)) / 100) + 1.0;
			e.setDamage(e.getDamage() * multiple);
		}
	}
}
