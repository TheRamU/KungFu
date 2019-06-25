package me.ram.kungfu;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bstats.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.ram.kungfu.command.Commands;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.data.PlayerData;
import me.ram.kungfu.gui.Menu;
import me.ram.kungfu.kungfu.Boxing;
import me.ram.kungfu.kungfu.Dart;
import me.ram.kungfu.kungfu.Dodge;
import me.ram.kungfu.kungfu.IronHead;
import me.ram.kungfu.kungfu.OverWall;
import me.ram.kungfu.kungfu.Stick;
import me.ram.kungfu.kungfu.Sword;
import me.ram.kungfu.kungfu.WaterWalk;
import me.ram.kungfu.listener.EventListener;
import me.ram.kungfu.network.UpdateCheck;
import me.ram.kungfu.placeholderapi.PAPIHook;

public class KungFu extends JavaPlugin {
	
	private static KungFu instance;
	private static Menu menu;
	private static PlayerData playerdata;
	private static UpdateCheck updatecheck;
	
	public void onEnable() {
		instance = this;
    	Bukkit.getConsoleSender().sendMessage("¡ìf========================================");
        Bukkit.getConsoleSender().sendMessage("¡ì7");
        Bukkit.getConsoleSender().sendMessage("                 ¡ìbKungFu");
        Bukkit.getConsoleSender().sendMessage("¡ì7");
        Bukkit.getConsoleSender().sendMessage(" ¡ìa°æ±¾: " + KungFu.getVersion());
        Bukkit.getConsoleSender().sendMessage("¡ì7");
        Bukkit.getConsoleSender().sendMessage(" ¡ìa×÷Õß: Ram");
        Bukkit.getConsoleSender().sendMessage("¡ì7");
        Bukkit.getConsoleSender().sendMessage("¡ìf========================================");
        init();
		menu = new Menu();
		playerdata = new PlayerData();
		playerdata.loadData();
	}
	
	private void init() {
		String prefix = "[" + this.getDescription().getName() + "] ";
		Bukkit.getConsoleSender().sendMessage(prefix + "¡ìf¿ªÊ¼¼ÓÔØ²å¼þ...");
    	try {
            Config.loadConfig();
        } catch (Exception e) {
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc´íÎó: ¡ìfÅäÖÃÎÄ¼þ¼ÓÔØÊ§°Ü£¡");
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc²å¼þ¼ÓÔØÊ§°Ü£¡");
        	Bukkit.getPluginManager().disablePlugin(instance);
        	return;
        }
        try {
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìfÕýÔÚ×¢²á¼àÌýÆ÷...");
            registerEvents();
            Bukkit.getConsoleSender().sendMessage(prefix + "¡ìa¼àÌýÆ÷×¢²á³É¹¦£¡");
        } catch (Exception e) {
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc´íÎó: ¡ìf¼àÌýÆ÷×¢²áÊ§°Ü£¡");
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc²å¼þ¼ÓÔØÊ§°Ü£¡");
        	Bukkit.getPluginManager().disablePlugin(instance);
        	return;
        }
        try {
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìfÕýÔÚ×¢²áÖ¸Áî...");
    		Bukkit.getPluginCommand("kungfu").setExecutor(new Commands());
            Bukkit.getConsoleSender().sendMessage(prefix + "¡ìaÖ¸Áî×¢²á³É¹¦£¡");
        } catch (Exception e) {
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc´íÎó: ¡ìfÖ¸Áî×¢²áÊ§°Ü£¡");
        	Bukkit.getConsoleSender().sendMessage(prefix + "¡ìc²å¼þ¼ÓÔØÊ§°Ü£¡");
        	Bukkit.getPluginManager().disablePlugin(instance);
        	return;
        }
        Bukkit.getConsoleSender().sendMessage(prefix + "¡ìa²å¼þ¼ÓÔØ³É¹¦£¡");
        try {
        	new Metrics(this);
        }catch (Exception e) {}
        updatecheck = new UpdateCheck();
        updatecheck.check();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        	new PAPIHook(this).hook();
        }
	}
	
	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		Bukkit.getPluginManager().registerEvents(new Boxing(), this);
		Bukkit.getPluginManager().registerEvents(new Dart(), this);
		Bukkit.getPluginManager().registerEvents(new Dodge(), this);
		Bukkit.getPluginManager().registerEvents(new IronHead(), this);
		Bukkit.getPluginManager().registerEvents(new OverWall(), this);
		Bukkit.getPluginManager().registerEvents(new Stick(), this);
		Bukkit.getPluginManager().registerEvents(new Sword(), this);
		Bukkit.getPluginManager().registerEvents(new WaterWalk(), this);
	}
	
	public static KungFu getInstance() {
		return instance;
	}
	
	public static String getVersion() {
		return "1.0.0";
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public PlayerData getPlayerData() {
		return playerdata;
	}
	
	public UpdateCheck getUpdateCheck() {
		return updatecheck;
	}
	
	public String getServerVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(46) + 1);
	}
	
	public boolean isItemInHand(Player player, Material type) {
        if (getServerVersion().startsWith("v1_8")) {
        	return player.getInventory().getItemInHand().getType().equals(type);
        }else if (player.getInventory().getItemInMainHand().getType().equals(type)) {
        	return true;
        }else if (player.getInventory().getItemInOffHand().getType().equals(type)) {
        	return true;
        }
		return false;
	}
	
	public boolean isItemInHand(Player player, String type) {
        if (getServerVersion().startsWith("v1_8")) {
        	return player.getInventory().getItemInHand().getType().name().contains(type);
        }else if (player.getInventory().getItemInMainHand().getType().name().contains(type)) {
        	return true;
        }else if (player.getInventory().getItemInOffHand().getType().name().contains(type)) {
        	return true;
        }
		return false;
	}
	
	public boolean hasItem(Player player, ItemStack item) {
		for (ItemStack itemStack : player.getInventory().getContents()) {
    		if (itemStack != null && itemStack.isSimilar(item)) {
    			return true;
    		}
		}
		return false;
	}
	
	public Material getMaterial(int id) {
		if (getServerVersion().startsWith("v1_13") || getServerVersion().startsWith("v1_14")) {
			for (Material type : Material.values()) {
				if (type.getId() == id) {
					return type;
				}
			}
		}else {
			try {
				return Material.getMaterial(id);
			}catch (Exception e) {
				return Material.AIR;
			}
		}
		return Material.AIR;
	}
	
	
	public KungFuType getKungFuType(String name) {
		try {
			return KungFuType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}
	
    public boolean isInteger(String string){
        Matcher matcher = Pattern.compile("^[+-]?[0-9]+$").matcher(string);
        return matcher.find();
    }
	
	public Map<KungFuType, Integer> getFullLevel() {
		Map<KungFuType, Integer> fulllevel = new HashMap<KungFuType, Integer>();
		fulllevel.put(KungFuType.Boxing, 3);
		fulllevel.put(KungFuType.Dart, 3);
		fulllevel.put(KungFuType.Dodge, 3);
		fulllevel.put(KungFuType.IronHead, 3);
		fulllevel.put(KungFuType.OverWall, 2);
		fulllevel.put(KungFuType.Stick, 3);
		fulllevel.put(KungFuType.Sword, 3);
		fulllevel.put(KungFuType.WaterWalk, 2);
		return fulllevel;
	}
}
