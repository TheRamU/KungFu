package me.ram.kungfu.database;

import org.bukkit.Bukkit;

import me.ram.kungfu.KungFu;
import me.ram.kungfu.utils.SQLUtil;

public class MySQL {
	public SQLUtil mysql;
	
	public String mysql_host;
	public String mysql_port;
	public String mysql_database;
	public String mysql_user;
	public String mysql_password;
	public String mysql_table;
	
	public void connectSQL() {
		String prefix = "[" + KungFu.getInstance().getDescription().getName() + "] ";
		Bukkit.getConsoleSender().sendMessage(prefix + "§f正在连接数据库...");
		mysql = new SQLUtil(new TypeField("name", "TEXT"), new TypeField("coin", "INTEGER"), new TypeField("level", "TEXT"), new TypeField("disable", "TEXT"));
		mysql.init(mysql_host, mysql_port, mysql_database, mysql_user, mysql_password);
		if (mysql.connect()) {
			Bukkit.getConsoleSender().sendMessage(prefix + "§a数据库连接成功！");
			mysql.createTable("kungfu");
		}else {
			Bukkit.getConsoleSender().sendMessage(prefix + "§c数据库连接失败！");
		}
	}
	
	public void setPlayerCoin(String player, int i) {
		if (mysql.isExist("name", player)) {
			mysql.setData("name", player, "coin", i);
		}else {
			mysql.addData("name", player, i, "", "");
		}
	}
	
	public void setPlayerLevel(String player, String level) {
		if (mysql.isExist("name", player)) {
			mysql.setData("name", player, "level", level);
		}else {
			mysql.addData("name", player, 0, level, "");
		}
	}
	
	public void setPlayerDisable(String player, String disable) {
		if (mysql.isExist("name", player)) {
			mysql.setData("name", player, "disable", disable);
		}else {
			mysql.addData("name", player, 0, "", disable);
		}
	}
	
	public Integer getPlayerCoin(String player) {
		if (mysql.isExist("name", player)) {
			return (Integer) mysql.getData("name", player, "coin");
		}
		return 0;
	}
	
	public String getPlayerLevel(String player) {
		if (mysql.isExist("name", player)) {
			return (String) mysql.getData("name", player, "level");
		}
		return "";
	}
	
	public String getPlayerDisable(String player) {
		if (mysql.isExist("name", player)) {
			return (String) mysql.getData("name", player, "disable");
		}
		return "";
	}
}
