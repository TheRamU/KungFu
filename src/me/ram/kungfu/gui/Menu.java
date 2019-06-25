package me.ram.kungfu.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.storage.PlayerStorage;

public class Menu {
	
	private Map<UUID, MenuType> omplayers = new HashMap<UUID, MenuType>();
	
	public void openMenu(Player player) {
		if (!isOpenMenu(player, MenuType.Main) && !isOpenMenu(player, MenuType.Manage)) {
			player.closeInventory();
		}
		Inventory inventory = Bukkit.createInventory(null, 54, Config.menu_title);
		setMainItem(player, inventory);
		player.openInventory(inventory);
		omplayers.put(player.getUniqueId(), MenuType.Main);
	}
	
	public void openManageMenu(Player player) {
		if (isOpenMenu(player, MenuType.Main)) {
			Inventory inventory = Bukkit.createInventory(null, 54, Config.menu_title);
			setManageItem(player, inventory);
			player.openInventory(inventory);
			omplayers.put(player.getUniqueId(), MenuType.Manage);
		}
	}
	
	public void openCraftMenu(Player player) {
		FileConfiguration config = Config.getCraftConfig();
		Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH);
		if (config.getKeys(false).contains("book")) {
			String[] string = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
			int i = 1;
			Set<String> set = config.getConfigurationSection("book").getKeys(false);
			for (String str : string) {
				if (set.contains(str)) {
					inventory.setItem(i, ItemStack.deserialize((Map<String, Object>) config.getList("book." + str).get(0)));
				}
				i++;
			}
		}
		inventory.setItem(0, Config.book_item);
		player.closeInventory();
		player.openInventory(inventory);
		omplayers.put(player.getUniqueId(), MenuType.Craft);
		player.updateInventory();
	}
	
	public void setMainItem(Player player, Inventory inventory) {
		ItemStack infoitem = new ItemStack(KungFu.getInstance().getMaterial(Config.menu_info_item_id), 1, Short.valueOf(Config.menu_info_item_damage.toString()));
		ItemMeta infometa = infoitem.getItemMeta();
		String playername = player.getDisplayName();
		if (playername == null) {
			playername = player.getName();
		}
		Integer coin = KungFu.getInstance().getPlayerData().getPlayerCoin(player.getName());
		infometa.setDisplayName(Config.menu_info_item_name.replace("{player}", playername).replace("{coin}", coin.toString()));
		List<String> infolore = new ArrayList<String>();
		for (String l : Config.menu_info_item_lore) {
			infolore.add(l.replace("{player}", playername).replace("{coin}", coin.toString()));
		}
		infometa.setLore(infolore);
		infoitem.setItemMeta(infometa);
		inventory.setItem(4, infoitem);
		inventory.setItem(49, Config.menu_close_item);
		inventory.setItem(53, Config.menu_manage_item);
		Map<KungFuType, Integer> fulllevel = KungFu.getInstance().getFullLevel();
		int i = 20;
		for (KungFuType type : Config.kungfu_enableds) {
			if (i == 25) {
				i = 29;
			}
			ItemStack kungfuitem = Config.kungfu_items.get(type).clone();
			ItemMeta kungfumeta = kungfuitem.getItemMeta();
			List<String> lore = kungfumeta.getLore();
			int level = PlayerStorage.getLevel(player, type);
			String state = Config.menu_item_state_study;
			if (level >= fulllevel.get(type)) {
				state = Config.menu_item_state_complete;
			}else if (level > 0) {
				state = Config.menu_item_state_advancement;
			}
			String kflevel = level + "";
			if (level < 1) {
				level = 1;
			}else if (level > fulllevel.get(type)) {
				level = fulllevel.get(type);
			}
			String value = Config.kungfu_level_value.get(type).get(level).toString();
			String cost = Config.kungfu_level_cost.get(type).get(level).toString();
			String damage = "";
			if (type.equals(KungFuType.Stick)) {
				damage = Config.kungfu_stick_level_damage.get(level).toString();
			}
			for (String l : Config.kungfu_lore.get(type)) {
				lore.add(l.replace("{cost}", cost).replace("{value}", value).replace("{state}", state).replace("{damage}", damage).replace("{level}", kflevel).replace("{fulllevel}", fulllevel.get(type).toString()));
			}
			kungfumeta.setLore(lore);
			kungfuitem.setItemMeta(kungfumeta);
			inventory.setItem(i, kungfuitem);
			i++;
		}
	}
	
	public void setManageItem(Player player, Inventory inventory) {
		inventory.clear();
		ItemStack infoitem = new ItemStack(KungFu.getInstance().getMaterial(Config.menu_info_item_id), 1, Short.valueOf(Config.menu_info_item_damage.toString()));
		ItemMeta infometa = infoitem.getItemMeta();
		String playername = player.getDisplayName();
		if (playername == null) {
			playername = player.getName();
		}
		Integer coin = KungFu.getInstance().getPlayerData().getPlayerCoin(player.getName());
		infometa.setDisplayName(Config.menu_info_item_name.replace("{player}", playername).replace("{coin}", coin.toString()));
		List<String> infolore = new ArrayList<String>();
		for (String l : Config.menu_info_item_lore) {
			infolore.add(l.replace("{player}", playername).replace("{coin}", coin.toString()));
		}
		infometa.setLore(infolore);
		infoitem.setItemMeta(infometa);
		inventory.setItem(4, infoitem);
		inventory.setItem(49, Config.menu_back_item);
		int i = 20;
		for (KungFuType type : Config.kungfu_enableds) {
			if (i == 25) {
				i = 29;
			}
			ItemStack kungfuitem = Config.kungfu_items.get(type).clone();
			ItemMeta kungfumeta = kungfuitem.getItemMeta();
			List<String> lore = kungfumeta.getLore();
			String state = Config.menu_item_state_enabled;
			if (PlayerStorage.isDisable(player, type)) {
				state = Config.menu_item_state_disable;
			}
			for (String l : Config.menu_manage_lore) {
				lore.add(l.replace("{state}", state));
			}
			kungfumeta.setLore(lore);
			kungfuitem.setItemMeta(kungfumeta);
			inventory.setItem(i, kungfuitem);
			i++;
		}
	}
	
	public boolean isOpenMenu(Player player, MenuType type) {
		if (omplayers.containsKey(player.getUniqueId())) {
			return omplayers.get(player.getUniqueId()).equals(type);
		}
		return false;
	}
	
	public void removePlayer(Player player) {
		if (omplayers.containsKey(player.getUniqueId())) {
			omplayers.remove(player.getUniqueId());
		}
	}
}
