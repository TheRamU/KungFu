package me.ram.kungfu.kungfu;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class Dart implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		Player player = (Player) e.getPlayer();
		if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || !KungFu.getInstance().isItemInHand(player, Material.ARROW)) {
			return;
		}
		KungFuType type = KungFuType.Dart;
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
		e.setCancelled(true);
		if (takeItem(player, Material.ARROW, 1)) {
			Arrow arrow = player.launchProjectile(Arrow.class);
			arrow.setShooter(player);
			arrow.setVelocity(player.getLocation().getDirection().multiply((int) Config.kungfu_level_value.get(type).get(level)));
		}
	}
	
	private boolean takeItem(Player player, Material type, int amount) {
		String version = KungFu.getInstance().getServerVersion();
        if (version.startsWith("v1_8")) {
        	ItemStack itemStack = player.getInventory().getItemInHand();
			if (itemStack != null && itemStack.getType().equals(type) && itemStack.getAmount() >= amount) {
				itemStack.setAmount(itemStack.getAmount() - amount);
				player.getInventory().setItemInHand(itemStack);
				return true;
			}
		}else {
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			if (itemStack != null && itemStack.getType().equals(type) && itemStack.getAmount() >= amount) {
				itemStack.setAmount(itemStack.getAmount() - amount);
				player.getInventory().setItemInMainHand(itemStack);
				return true;
			}
			itemStack = player.getInventory().getItemInOffHand();
			if (itemStack != null && itemStack.getType().equals(type) && itemStack.getAmount() >= amount) {
				itemStack.setAmount(itemStack.getAmount() - amount);
				player.getInventory().setItemInOffHand(itemStack);
				return true;
			}
		}
		return false;
	}
}
