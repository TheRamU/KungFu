package me.ram.kungfu.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.gui.MenuType;
import me.ram.kungfu.storage.PlayerStorage;
import me.ram.kungfu.utils.SoundUtil;

public class EventListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		Inventory inventory = e.getInventory();
		int slot = e.getRawSlot();
		if (KungFu.getInstance().getMenu().isOpenMenu(player, MenuType.Craft)) {
			if (slot == 0) {
				e.setCancelled(true);
			}
			if (e.isShiftClick()) {
				e.setCancelled(true);
			}
			new BukkitRunnable() {
	    		@Override
	            public void run() {
	    			player.updateInventory();
	    		}
			}.runTaskLater(KungFu.getInstance(), 3L);
		} else if (KungFu.getInstance().getMenu().isOpenMenu(player, MenuType.Main)) {
			e.setCancelled(true);
			if (!e.isLeftClick()) {
				return;
			}
			if (slot == 49) {
				player.playSound(player.getLocation(), SoundUtil.getSound("CLICK", "UI_BUTTON_CLICK"), 0.35f, 1f);
				player.closeInventory();
				return;
			}
			if (slot == 53) {
				player.playSound(player.getLocation(), SoundUtil.getSound("CLICK", "UI_BUTTON_CLICK"), 0.35f, 1f);
				KungFu.getInstance().getMenu().openManageMenu(player);
				return;
			}
			ItemStack itemStack = e.getCurrentItem();
			if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (itemMeta.hasLore()) {
					KungFuType type = KungFu.getInstance().getKungFuType(itemMeta.getLore().get(0).replace("¡ì", ""));
					if (type != null) {
						int level = PlayerStorage.getLevel(player, type);
						int coin = KungFu.getInstance().getPlayerData().getPlayerCoin(player.getName());
						if (level < KungFu.getInstance().getFullLevel().get(type)) {
							if (level < 1) {
								level = 1;
							}
							int cost = Config.kungfu_level_cost.get(type).get(level);
							if (coin >= cost) {
								Map<String, Integer> kflevel = PlayerStorage.getLevel(player);
								kflevel.put(type.toString(), kflevel.getOrDefault(type.toString(), 0) + 1);
								String str = "";
								for (String kf : kflevel.keySet()) {
									str = str + (str.length() > 0 ? ", " : "") + kf + ":" + kflevel.get(kf).toString();
								}
								KungFu.getInstance().getPlayerData().setPlayerCoin(player.getName(), coin - cost);
								KungFu.getInstance().getPlayerData().setPlayerLevel(player.getName(), str);
								player.closeInventory();
								player.sendMessage(Config.message_studied.replace("{kungfu}", Config.kungfu_names.get(type)).replace("{level}", kflevel.get(type.toString()).toString()));
								player.playSound(player.getLocation(), SoundUtil.getSound("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1f, 1f);
							}else {
								player.playSound(player.getLocation(), SoundUtil.getSound("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT"), 1f, 0.5f);
								player.sendMessage(Config.message_no_coin);
							}
						}
					}
				}
			}
		}else if (KungFu.getInstance().getMenu().isOpenMenu(player, MenuType.Manage)) {
			e.setCancelled(true);
			if (!e.isLeftClick()) {
				return;
			}
			if (slot == 49) {
				player.playSound(player.getLocation(), SoundUtil.getSound("CLICK", "UI_BUTTON_CLICK"), 0.35f, 1f);
				KungFu.getInstance().getMenu().openMenu(player);
				return;
			}
			ItemStack itemStack = e.getCurrentItem();
			if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
				ItemMeta itemMeta = itemStack.getItemMeta();
				if (itemMeta.hasLore()) {
					KungFuType type = KungFu.getInstance().getKungFuType(itemMeta.getLore().get(0).replace("¡ì", ""));
					if (type != null) {
						List<String> list = new ArrayList<String>();
						list.addAll(PlayerStorage.getDisable(player));
						if (list.contains(type.toString())) {
							list.remove(type.toString());
						}else {
							list.add(type.toString());
						}
						KungFu.getInstance().getPlayerData().setPlayerDdisable(player.getName(), list);
						KungFu.getInstance().getMenu().setManageItem(player, inventory);
						player.playSound(player.getLocation(), SoundUtil.getSound("CLICK", "UI_BUTTON_CLICK"), 0.35f, 1f);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					PlayerStorage.setLevel(player, KungFu.getInstance().getPlayerData().getPlayerLevel(player.getName()));
				}
			}
		}.runTaskLater(KungFu.getInstance(), 1L);
		if (Config.book_join_give) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (player.isOnline() && !KungFu.getInstance().hasItem(player, Config.book_item)) {
						player.getInventory().addItem(Config.book_item);
					}
				}
			}.runTaskLater(KungFu.getInstance(), 1L);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				KungFu.getInstance().getUpdateCheck().playerJoin(player);
			}
		}.runTaskLater(KungFu.getInstance(), 5L);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack itemStack = e.getItem();
		if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && itemStack != null && itemStack.isSimilar(Config.book_item)) {
			e.setCancelled(true);
			KungFu.getInstance().getMenu().openMenu(player);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e) {
		Inventory inventory = e.getInventory();
		Player player = (Player) e.getPlayer();
		if (KungFu.getInstance().getMenu().isOpenMenu(player, MenuType.Craft)) {
			String[] iid = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
			int i = 1;
			for (String id : iid) {
				ItemStack itemStack = inventory.getItem(i);
				if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
					List<Object> list = new ArrayList<Object>();
					list.add(itemStack.serialize());
					Config.setCraft("book." + id, list);
				}else {
					Config.setCraft("book." + id, null);
				}
				i++;
			}
			player.sendMessage(Config.message_edit_craft);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCloseHighest(InventoryCloseEvent e) {
		KungFu.getInstance().getMenu().removePlayer((Player) e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		KungFu.getInstance().getMenu().removePlayer(e.getPlayer());
	}
}
