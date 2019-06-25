package me.ram.kungfu.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.storage.PlayerStorage;
import me.ram.kungfu.utils.ColorUtil;

public class Config {
	
	public static boolean update_check;
	public static boolean mysql_enabled;
	public static String mysql_host;
	public static String mysql_port;
	public static String mysql_database;
	public static String mysql_user;
	public static String mysql_password;
	public static String mysql_table;
	public static ItemStack book_item;
	public static boolean book_join_give;
	public static boolean book_craft;
	public static String menu_title;
	public static Integer menu_info_item_id;
	public static Integer menu_info_item_damage;
	public static String menu_info_item_name;
	public static List<String> menu_info_item_lore;
	public static ItemStack menu_close_item;
	public static ItemStack menu_manage_item;
	public static ItemStack menu_back_item;
	public static String menu_item_state_study;
	public static String menu_item_state_advancement;
	public static String menu_item_state_complete;
	public static String menu_item_state_enabled;
	public static String menu_item_state_disable;
	public static List<String> menu_manage_lore;
	public static List<KungFuType> kungfu_enableds;
	public static Map<KungFuType, String> kungfu_names;
	public static Map<KungFuType, ItemStack> kungfu_items;
	public static Map<KungFuType, List<String>> kungfu_lore;
	public static Map<KungFuType, Map<Integer, Integer>> kungfu_level_cost;
	public static Map<KungFuType, Map<Integer, Object>> kungfu_level_value;
	public static Map<Integer, Double> kungfu_stick_level_damage;
	public static String message_studied;
	public static String message_no_coin;
	public static String message_look_coin;
	public static String message_set_coin;
	public static String message_give_coin;
	public static String message_take_coin;
	public static String message_look_kungfu;
	public static String message_set_kungfu;
	public static String message_toggle_kungfu;
	public static String message_edit_craft;
	public static String message_help_prefix;
	public static String message_help_main;
	public static String message_help_menu;
	public static String message_help_guide;
	public static String message_help_help;
	public static String message_help_reload;
	public static String message_help_craft;
	public static String message_help_coin;
	public static String message_help_coin_look;
	public static String message_help_coin_set;
	public static String message_help_coin_give;
	public static String message_help_coin_take;
	public static String message_help_kungfu;
	public static String message_help_kungfu_look;
	public static String message_help_kungfu_set;
	public static String message_help_kungfu_toggle;
	public static String message_unknown_command;
	public static String message_unknown_kungfu;
	public static String message_no_permission;
	public static String message_invalid_integer;
	public static String message_reload;
	
	public static void loadConfig() {
		String prefix = "[" + KungFu.getInstance().getDescription().getName() + "] ";
		Bukkit.getConsoleSender().sendMessage(prefix + "§f正在加载配置文件...");
		KungFu.getInstance().saveDefaultConfig();
		KungFu.getInstance().reloadConfig();
		Bukkit.getConsoleSender().sendMessage(prefix + "§a默认配置文件已保存！");
		FileConfiguration config = KungFu.getInstance().getConfig();
		update_check = config.getBoolean("update_check");
		mysql_enabled = config.getBoolean("mysql.enabled");
		mysql_host = config.getString("mysql.host");
		mysql_port = config.getString("mysql.port");
		mysql_database = config.getString("mysql.database");
		mysql_user = config.getString("mysql.user");
		mysql_password = config.getString("mysql.password");
		mysql_table = config.getString("mysql.table");
		book_join_give = config.getBoolean("book.join_give");
		book_join_give = config.getBoolean("book.craft");
		book_item = toItemStack(config.getInt("book.item.id"), config.getInt("book.item.damage"), ColorUtil.color(config.getString("book.item.name")), ColorUtil.listcolor(config.getStringList("book.item.lore")));
		menu_title = ColorUtil.color(config.getString("menu.title")) + "§k§f§m§e§n§u";
		menu_info_item_id = config.getInt("menu.info_item.id");
		menu_info_item_damage = config.getInt("menu.info_item.damage");
		menu_info_item_name = ColorUtil.color(config.getString("menu.info_item.name"));
		menu_info_item_lore = ColorUtil.listcolor(config.getStringList("menu.info_item.lore"));
		menu_close_item = toItemStack(config.getInt("menu.close_item.id"), config.getInt("menu.close_item.damage"), ColorUtil.color(config.getString("menu.close_item.name")), ColorUtil.listcolor(config.getStringList("menu.close_item.lore")));
		menu_manage_item = toItemStack(config.getInt("menu.manage_item.id"), config.getInt("menu.manage_item.damage"), ColorUtil.color(config.getString("menu.manage_item.name")), ColorUtil.listcolor(config.getStringList("menu.manage_item.lore")));
		menu_back_item = toItemStack(config.getInt("menu.back_item.id"), config.getInt("menu.back_item.damage"), ColorUtil.color(config.getString("menu.back_item.name")), ColorUtil.listcolor(config.getStringList("menu.back_item.lore")));
		menu_item_state_study = ColorUtil.color(config.getString("menu.item_state.study"));
		menu_item_state_advancement = ColorUtil.color(config.getString("menu.item_state.advancement"));
		menu_item_state_complete = ColorUtil.color(config.getString("menu.item_state.complete"));
		menu_item_state_enabled = ColorUtil.color(config.getString("menu.item_state.enabled"));
		menu_item_state_disable = ColorUtil.color(config.getString("menu.item_state.disable"));
		menu_manage_lore = ColorUtil.listcolor(config.getStringList("menu.manage_lore"));
		kungfu_enableds = new ArrayList<KungFuType>();
		kungfu_names = new HashMap<KungFuType, String>();
		kungfu_items = new HashMap<KungFuType, ItemStack>();
		kungfu_lore = new HashMap<KungFuType, List<String>>();
		kungfu_level_cost = new HashMap<KungFuType, Map<Integer, Integer>>();
		kungfu_level_value = new HashMap<KungFuType, Map<Integer, Object>>();
		for (KungFuType type : KungFuType.values()) {
			if (config.getBoolean("kungfu." + type.toString() + ".enabled")) {
				kungfu_enableds.add(type);
				kungfu_names.put(type, ColorUtil.color(config.getString("kungfu." + type.toString() + ".name")));
				kungfu_items.put(type, toItemStack(config.getInt("kungfu." + type.toString() + ".item.id"), config.getInt("kungfu." + type.toString() + ".item.damage"), ColorUtil.color(config.getString("kungfu." + type.toString() + ".item.name")), type.toString()));
				kungfu_lore.put(type, ColorUtil.listcolor(config.getStringList("kungfu." + type.toString() + ".item.lore")));
				kungfu_level_cost.put(type, new HashMap<Integer, Integer>());
				kungfu_level_value.put(type, new HashMap<Integer, Object>());
				int i = 1;
				for (;;) {
					if (config.get("kungfu." + type.toString() + ".level." + i) != null) {
						kungfu_level_cost.get(type).put(i, config.getInt("kungfu." + type.toString() + ".level." + i + ".cost"));
						kungfu_level_value.get(type).put(i, config.getInt("kungfu." + type.toString() + ".level." + i + ".value"));
						i++;
					}else {
						break;
					}
				}
				if (type.equals(KungFuType.Stick)) {
					kungfu_stick_level_damage = new HashMap<Integer, Double>();
					for (int d = 1; d < 4; d ++) {
						kungfu_stick_level_damage.put(d, config.getDouble("kungfu." + type.toString() + ".level." + d + ".damage"));
					}
				}
			}
		}
		loadMessage();
		loadCraft();
		Bukkit.getConsoleSender().sendMessage(prefix + "§a配置文件加载成功！");
		PlayerStorage.update();
	}
	
	public static void setPlayerData(String player, String path, Object data) {
		File folder = new File(KungFu.getInstance().getDataFolder(),"/players");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/" + player + ".yml");
		if (file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(path, data);
		try {
			config.save(file);
		} catch (IOException e) {}
	}
	
	public static Object getPlayerData(String player, String path) {
		File folder = new File(KungFu.getInstance().getDataFolder(),"/players");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/" + player + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.get(path);
	}
	
	private static void loadMessage() {
		File folder = new File(KungFu.getInstance().getDataFolder(),"/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/message.yml");
		if (!file.exists()) {
			KungFu.getInstance().saveResource("message.yml", false);
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		message_studied = ColorUtil.color(config.getString("studied"));
		message_no_coin = ColorUtil.color(config.getString("no_coin"));
		message_look_coin = ColorUtil.color(config.getString("look_coin"));
		message_set_coin = ColorUtil.color(config.getString("set_coin"));
		message_give_coin = ColorUtil.color(config.getString("give_coin"));
		message_take_coin = ColorUtil.color(config.getString("take_coin"));
		message_look_kungfu = ColorUtil.color(config.getString("look_kungfu"));
		message_set_kungfu = ColorUtil.color(config.getString("set_kungfu"));
		message_toggle_kungfu = ColorUtil.color(config.getString("toggle_kungfu"));
		message_edit_craft = ColorUtil.color(config.getString("edit_craft"));
		message_help_prefix = ColorUtil.color(config.getString("help_prefix"));
		message_help_main = ColorUtil.color(config.getString("help_main"));
		message_help_menu = ColorUtil.color(config.getString("help_menu"));
		message_help_guide = ColorUtil.color(config.getString("help_guide"));
		message_help_help = ColorUtil.color(config.getString("help_help"));
		message_help_reload = ColorUtil.color(config.getString("help_reload"));
		message_help_craft = ColorUtil.color(config.getString("help_craft"));
		message_help_coin = ColorUtil.color(config.getString("help_coin"));
		message_help_coin_look = ColorUtil.color(config.getString("help_coin_look"));
		message_help_coin_set = ColorUtil.color(config.getString("help_coin_set"));
		message_help_coin_give = ColorUtil.color(config.getString("help_coin_give"));
		message_help_coin_take = ColorUtil.color(config.getString("help_coin_take"));
		message_help_kungfu = ColorUtil.color(config.getString("help_kungfu"));
		message_help_kungfu_look = ColorUtil.color(config.getString("help_kungfu_look"));
		message_help_kungfu_set = ColorUtil.color(config.getString("help_kungfu_set"));
		message_help_kungfu_toggle = ColorUtil.color(config.getString("help_kungfu_toggle"));
		message_unknown_command = ColorUtil.color(config.getString("unknown_command"));
		message_unknown_kungfu = ColorUtil.color(config.getString("unknown_kungfu"));
		message_no_permission = ColorUtil.color(config.getString("no_permission"));
		message_invalid_integer = ColorUtil.color(config.getString("invalid_integer"));
		message_reload = ColorUtil.color(config.getString("reload"));
	}
	
	private static void loadCraft() {
		if (KungFu.getInstance().getServer().getRecipesFor(Config.book_item).size() > 1) {
			return;
		}
		File folder = new File(KungFu.getInstance().getDataFolder(),"/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/craft.yml");
		if (!file.exists()) {
			KungFu.getInstance().saveResource("craft.yml", false);
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (config.getKeys(false).contains("book")) {
			ShapedRecipe shapedRecipe = new ShapedRecipe(Config.book_item);
			shapedRecipe.shape(new String[] { "ABC", "DEF", "GHI" });
			String[] string = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
			int i = 0;
			Set<String> set = config.getConfigurationSection("book").getKeys(false);
			for (String str : string) {
				if (set.contains(str)) {
					shapedRecipe.setIngredient("ABCDEFGHI".charAt(i), ItemStack.deserialize((Map<String, Object>) config.getList("book." + str).get(0)).getData());
				}
				i++;
			}
	        KungFu.getInstance().getServer().addRecipe(shapedRecipe);
		}
	}
	
	public static void setCraft(String path, Object object) {
		File folder = new File(KungFu.getInstance().getDataFolder(),"/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/craft.yml");
		if (!file.exists()) {
			KungFu.getInstance().saveResource("craft.yml", false);
		}
		FileConfiguration filec = YamlConfiguration.loadConfiguration(file);
		filec.set(path, object);
		try {
			filec.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FileConfiguration getCraftConfig() {
		File folder = new File(KungFu.getInstance().getDataFolder(),"/");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder.getAbsolutePath() + "/craft.yml");
		if (!file.exists()) {
			KungFu.getInstance().saveResource("craft.yml", false);
		}
		return YamlConfiguration.loadConfiguration(file);
	}
	
	private static ItemStack toItemStack(int id, int damage, String name, String type) {
		try {
			ItemStack itemStack = new ItemStack(KungFu.getInstance().getMaterial(id), 1, (short) damage);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(name);
			String l = type;
	    	l = "§" + l.replaceAll ("(.{1})", "$1§");
	    	l = l.substring(0,l.length() - 1);
	    	itemMeta.setLore(Arrays.asList(l));
	    	itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	itemStack.setItemMeta(itemMeta);
	    	return itemStack;
		}catch (Exception e) {
			return new ItemStack(Material.AIR);
		}
	}
	
	private static ItemStack toItemStack(int id, int damage, String name, List<String> lore) {
		try {
			ItemStack itemStack = new ItemStack(KungFu.getInstance().getMaterial(id), 1, (short) damage);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(name);
	    	itemMeta.setLore(lore);
	    	itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	itemStack.setItemMeta(itemMeta);
			return itemStack;
		}catch (Exception e) {
			return new ItemStack(Material.AIR);
		}
	}
}
