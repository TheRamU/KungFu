package me.ram.kungfu.kungfu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockIterator;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class Stick implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getDamager();
		Entity entity = e.getEntity();
		if (entity.hasMetadata("KungFu-Stick")) {
			entity.removeMetadata("KungFu-Stick", KungFu.getInstance());
			return;
		}
		if (!KungFu.getInstance().isItemInHand(player, Material.STICK) || !entity.getWorld().equals(player.getWorld()) || entity.getLocation().distance(player.getLocation()) > 4) {
			return;
		}
		stick(player, entity, e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (KungFu.getInstance().isItemInHand(player, Material.STICK) && (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
			stick(player, null, 0);
		}
	}
	
	private Double stick(Player player, Entity entity, double d) {
		KungFuType type = KungFuType.Stick;
		if (PlayerStorage.isDisable(player, type)) {
			return d;
		}
		int level = PlayerStorage.getLevel(player, type);
		if (level < 1) {
			return d;
		}
		if (level > KungFu.getInstance().getFullLevel().get(type)) {
			level = KungFu.getInstance().getFullLevel().get(type);
		}
		for (Entity ent : player.getNearbyEntities(2.5, 2, 2.5)) {
			if (player.getEntityId() != ent.getEntityId() && ent instanceof LivingEntity && (entity == null || entity.getEntityId() != ent.getEntityId())) {
				Location playerloc = player.getLocation().clone().add(0, 1, 0);
				Location entityloc = ent.getLocation().clone();
				double yaw = Math.toDegrees(Math.atan((playerloc.getX() - entityloc.getX()) / (playerloc.getZ() - entityloc.getZ())));
				yaw = playerloc.getZ() < entityloc.getZ() ? -yaw : (-yaw + 180);
				double pyaw = playerloc.getYaw();
				playerloc.setYaw((float) yaw);
				yaw = yaw - pyaw;
				yaw = yaw > 180 ? yaw - 360 : yaw;
				yaw = yaw < -180 ? yaw + 360 : yaw;
				yaw = yaw > 0 ? yaw : -yaw;
				double xd = playerloc.getX() > entityloc.getX() ? (playerloc.getX() - entityloc.getX()) : (entityloc.getX() - playerloc.getX());
				double zd = playerloc.getZ() > entityloc.getZ() ? (playerloc.getZ() - entityloc.getZ()) : (entityloc.getZ() - playerloc.getZ());
				double pitch = 0;
				if (xd > zd) {
					pitch = Math.toDegrees(Math.atan((playerloc.getY() - entityloc.getY()) / (playerloc.getX() - entityloc.getX())));
				}else {
					pitch = Math.toDegrees(Math.atan((playerloc.getY() - entityloc.getY()) / (playerloc.getZ() - entityloc.getZ())));
				}
				pitch = playerloc.getY() > entityloc.getY() ? (pitch < 0 ? -pitch : pitch) : (pitch > 0 ? -pitch : pitch);
				playerloc.setPitch((float) pitch);
				if (yaw <= ((int) Config.kungfu_level_value.get(type).get(level) / 2)) {
					try {
						List<Block> list = getLineOfSight(playerloc, (int) playerloc.distance(entityloc));
						boolean damage = true;
						for (Block block : list) {
							if (!block.getType().equals(Material.AIR)) {
								damage = false;
								break;
							}
						}
						if (damage) {
							ent.setMetadata("KungFu-Stick", new FixedMetadataValue(KungFu.getInstance(), System.currentTimeMillis()));
							LivingEntity livingEntity = (LivingEntity) ent;
							livingEntity.damage(Config.kungfu_stick_level_damage.get(level), player);
						}
					}catch (Exception e) {}
				}
			}
		}
		return d + Double.valueOf(Config.kungfu_stick_level_damage.get(level));
	}
	
    private List<Block> getLineOfSight(Location location, int max) {
        List<Block> blocks = new ArrayList<Block>();
        Iterator<Block> iterator = new BlockIterator(location, 1, max);
        while (iterator.hasNext()) {
            Block block = iterator.next();
            blocks.add(block);
            if (!block.getType().equals(Material.AIR)) {
                break;
            }
        }
        return blocks;
    }
}
