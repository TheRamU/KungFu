package me.ram.kungfu.kungfu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class Dodge implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (!(e.getEntity() instanceof Player) || !e.getCause().equals(DamageCause.FALL)) {
			return;
		}
		Player player = (Player) e.getEntity();
		KungFuType type = KungFuType.Dodge;
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
		double multiple = 1.0 - Double.valueOf((int) Config.kungfu_level_value.get(type).get(level)) / 100;
		e.setDamage(e.getDamage() * multiple);
	}
}
