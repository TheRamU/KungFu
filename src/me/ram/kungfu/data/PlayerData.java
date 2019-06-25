package me.ram.kungfu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import me.ram.kungfu.config.Config;
import me.ram.kungfu.database.MySQL;
import me.ram.kungfu.storage.PlayerStorage;

public class PlayerData {
	
	private MySQL mysql;
	
	public void loadData() {
		if (Config.mysql_enabled) {
			mysql = new MySQL();
			mysql.mysql_host = Config.mysql_host;
			mysql.mysql_port = Config.mysql_port;
			mysql.mysql_database = Config.mysql_database;
			mysql.mysql_user = Config.mysql_user;
			mysql.mysql_password = Config.mysql_password;
			mysql.mysql_table = Config.mysql_table;
			mysql.connectSQL();
		}
	}
	
	public Integer getPlayerCoin(String player) {
		int coin = 0;
		if (Config.mysql_enabled) {
			coin = mysql.getPlayerCoin(player);
		}else {
			Object object = Config.getPlayerData(player, "coin");
			if (object != null) {
				coin = (int) object;
			}
		}
		return coin;
	}
	
	public Map<String, Integer> getPlayerLevel(String player) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Object object = null;
		if (Config.mysql_enabled) {
			object = mysql.getPlayerLevel(player);
		}else {
			object = Config.getPlayerData(player, "level");
		}
		String str = "";
		if (object != null) {
			str = (String) object;
		}
		try {
			for (String kungfu : str.split(", ")) {
				try {
					String[] args = kungfu.split(":");
					map.put(args[0], Integer.valueOf(args[1]));
				} catch (Exception e) {}
			}
		} catch (Exception e) {}
		return map;
	}
	
	public List<String> getPlayerDisable(String player) {
		List<String> list = new ArrayList<String>();
		Object object = null;
		if (Config.mysql_enabled) {
			object = mysql.getPlayerDisable(player);
		}else {
			object = Config.getPlayerData(player, "disable");
		}
		String str = "";
		if (object != null) {
			str = (String) object;
		}
		try {
			for (String kungfu : str.split(", ")) {
				list.add(kungfu);
			}
		} catch (Exception e) {}
		return list;
	}
	
	public void setPlayerCoin(String player, Integer i) {
		if (Config.mysql_enabled) {
			mysql.setPlayerCoin(player, i);
		}else {
			Config.setPlayerData(player, "coin", i);
		}
	}
	
	public void setPlayerLevel(String player, String data) {
		if (Config.mysql_enabled) {
			mysql.setPlayerLevel(player, data);
		}else {
			Config.setPlayerData(player, "level", data);
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			for (String kungfu : data.split(", ")) {
				try {
					String[] args = kungfu.split(":");
					map.put(args[0], Integer.valueOf(args[1]));
				} catch (Exception e) {}
			}
		} catch (Exception e) {}
		PlayerStorage.setLevel(Bukkit.getPlayer(player), map);
	}
	
	public void setPlayerDdisable(String player, List<String> data) {
		String str = "";
		for (String l : data) {
			str = str + (str.length() > 0 ? ", " : "") + l;
		}
		if (Config.mysql_enabled) {
			mysql.setPlayerDisable(player, str);
		}else {
			Config.setPlayerData(player, "disable", str);
		}
		PlayerStorage.setDisable(Bukkit.getPlayer(player), data);
	}
}
