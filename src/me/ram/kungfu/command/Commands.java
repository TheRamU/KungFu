package me.ram.kungfu.command;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ram.kungfu.KungFuType;
import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kungfu")) {
        	if (args.length == 0) {
        		sender.sendMessage("¡ìf==========================================================");
        		sender.sendMessage("");
        		sender.sendMessage("                          ¡ìbKungFu");
        		sender.sendMessage("");
        		sender.sendMessage("¡ìf  °æ±¾: ¡ìa" + KungFu.getVersion());
        		sender.sendMessage("");
        		sender.sendMessage("¡ìf  ×÷Õß: ¡ìaRam");
        		sender.sendMessage("");
        		sender.sendMessage("¡ìf==========================================================");
        		return true;
        	}
        	if (args[0].equalsIgnoreCase("help")) {
        		sender.sendMessage("¡ìf===========================================================");
        		sender.sendMessage("");
        		sender.sendMessage("¡ìb¡ìl KungFu ¡ìfv" + KungFu.getVersion() + "  ¡ì7by Ram");
        		sender.sendMessage("");
        		sender.sendMessage(Config.message_help_main);
        		sender.sendMessage(Config.message_help_menu);
        		sender.sendMessage(Config.message_help_guide);
        		sender.sendMessage(Config.message_help_help);
        		sender.sendMessage(Config.message_help_craft);
        		sender.sendMessage(Config.message_help_coin);
        		sender.sendMessage(Config.message_help_kungfu);
        		sender.sendMessage(Config.message_help_reload);
        		sender.sendMessage("");
        		sender.sendMessage("¡ìf===========================================================");
        		return true;
        	}
        	if (args[0].equalsIgnoreCase("menu")) {
        		if (!(sender instanceof Player)) {
        			return true;
        		}
        		if (sender.hasPermission("kungfu.menu")) {
        			KungFu.getInstance().getMenu().openMenu((Player) sender);
        		}else {
        			sender.sendMessage(Config.message_no_permission);
        		}
        		return true;
        	}
        	if (args[0].equalsIgnoreCase("guide")) {
        		if (!(sender instanceof Player)) {
        			return true;
        		}
        		if (sender.hasPermission("kungfu.guide")) {
        			((Player) sender).getInventory().addItem(Config.book_item);
        		}else {
        			sender.sendMessage(Config.message_no_permission);
        		}
        		return true;
        	}
        	if (args[0].equalsIgnoreCase("reload")) {
        		if (sender.hasPermission("kungfu.reload")) {
        			Config.loadConfig();
        			KungFu.getInstance().getPlayerData().loadData();
        			sender.sendMessage(Config.message_reload);
        		}else {
        			sender.sendMessage(Config.message_no_permission);
        		}
        		return true;
        	}
        	if (args[0].equalsIgnoreCase("craft")) {
        		if (sender.hasPermission("kungfu.craft")) {
        			if (!(sender instanceof Player)) {
        				return true;
        			}
        			Player player = (Player) sender;
        			KungFu.getInstance().getMenu().openCraftMenu(player);
        			return true;
        		}else {
        			sender.sendMessage(Config.message_no_permission);
        			return true;
        		}
    		}
    		if (args[0].equalsIgnoreCase("coin")) {
				if (args.length == 1) {
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_look);
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_set);
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_give);
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_take);
	        		return true;
				}
				if (args[1].equalsIgnoreCase("look")) {
					if (sender.hasPermission("kungfu.coin.look")) {
						if (args.length == 3) {
							sender.sendMessage(Config.message_look_coin.replace("{player}", args[2]).replace("{coin}", KungFu.getInstance().getPlayerData().getPlayerCoin(args[2]) + ""));
							return true;
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_look);
							return true;
						}
	        		}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
				if (args[1].equalsIgnoreCase("set")) {
					if (sender.hasPermission("kungfu.coin.set")) {
						if (args.length == 4) {
							try {
								Integer.valueOf(args[3]);
							} catch (Exception e) {
								sender.sendMessage(Config.message_invalid_integer);
								return true;
							}
							KungFu.getInstance().getPlayerData().setPlayerCoin(args[2], Integer.valueOf(args[3]));
							sender.sendMessage(Config.message_set_coin.replace("{player}", args[2]).replace("{coin}", args[3]));
							return true;
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_set);
							return true;
						}
	        		}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
				if (args[1].equalsIgnoreCase("give")) {
					if (sender.hasPermission("kungfu.coin.give")) {
						if (args.length == 4) {
							try {
								Integer.valueOf(args[3]);
							} catch (Exception e) {
								sender.sendMessage(Config.message_invalid_integer);
								return true;
							}
							KungFu.getInstance().getPlayerData().setPlayerCoin(args[2], KungFu.getInstance().getPlayerData().getPlayerCoin(args[2]) + Integer.valueOf(args[3]));
							sender.sendMessage(Config.message_give_coin.replace("{player}", args[2]).replace("{coin}", args[3]));
							return true;
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_give);
							return true;
						}
	        		}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
				if (args[1].equalsIgnoreCase("take")) {
					if (sender.hasPermission("kungfu.coin.take")) {
						if (args.length == 4) {
							try {
								Integer.valueOf(args[3]);
							} catch (Exception e) {
								sender.sendMessage(Config.message_invalid_integer);
								return true;
							}
							int i = KungFu.getInstance().getPlayerData().getPlayerCoin(args[2]) - Integer.valueOf(args[3]);
							if (i < 0) {
								i = 0;
							}
							KungFu.getInstance().getPlayerData().setPlayerCoin(args[2], i);
							sender.sendMessage(Config.message_take_coin.replace("{player}", args[2]).replace("{coin}", args[3]));
							return true;
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_take);
							return true;
						}
	        		}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_look);
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_set);
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_give);
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_coin_take);
        		return true;
    		}
    		if (args[0].equalsIgnoreCase("kungfu")) {
				if (args.length == 1) {
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_look);
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_set);
	        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_toggle);
	        		return true;
				}
				if (args[1].equalsIgnoreCase("look")) {
					if (sender.hasPermission("kungfu.kungfu.look")) {
						if (args.length == 4) {
							KungFuType type = KungFu.getInstance().getKungFuType(args[3]);
							if (type == null) {
								String kfs = "";
								for (KungFuType kf : KungFuType.values()) {
									kfs = kfs + (kfs.length() > 0 ? ", " : "") + kf.toString();
								}
								sender.sendMessage(Config.message_unknown_kungfu.replace("{kungfu}", kfs));
								return true;
							}else {
								String level = KungFu.getInstance().getPlayerData().getPlayerLevel(args[2]).get(type.toString()) + "";
								if (level.equals("null")) {
									level = "0";
								}
								sender.sendMessage(Config.message_look_kungfu.replace("{level}", level).replace("{player}", args[2]));
								return true;
							}
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_look);
							return true;
						}
					}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
				if (args[1].equalsIgnoreCase("set")) {
					if (sender.hasPermission("kungfu.kungfu.set")) {
						if (args.length == 5) {
							KungFuType type = KungFu.getInstance().getKungFuType(args[3]);
							if (type == null) {
								String kfs = "";
								for (KungFuType kf : KungFuType.values()) {
									kfs = kfs + (kfs.length() > 0 ? ", " : "") + kf.toString();
								}
								sender.sendMessage(Config.message_unknown_kungfu.replace("{kungfu}", kfs));
								return true;
							}else {
								try {
									Integer.valueOf(args[4]);
								} catch (Exception e) {
									sender.sendMessage(Config.message_invalid_integer);
									return true;
								}
								int level = Integer.valueOf(args[4]);
								if (level < 0 || level > KungFu.getInstance().getFullLevel().get(type)) {
									sender.sendMessage(Config.message_invalid_integer);
									return true;
								}
								Map<String, Integer> kflevel = KungFu.getInstance().getPlayerData().getPlayerLevel(args[2]);
								kflevel.put(type.toString(), level);
								String str = "";
								for (String kf : kflevel.keySet()) {
									str = str + (str.length() > 0 ? ", " : "") + kf + ":" + kflevel.get(kf).toString();
								}
								KungFu.getInstance().getPlayerData().setPlayerLevel(args[2], str);
								sender.sendMessage(Config.message_set_kungfu.replace("{level}", level + "").replace("{player}", args[2]));
								return true;
							}
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_set);
							return true;
						}
					}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
				if (args[1].equalsIgnoreCase("toggle")) {
					if (sender.hasPermission("kungfu.kungfu.toggle")) {
						if (args.length == 4) {
							KungFuType type = KungFu.getInstance().getKungFuType(args[3]);
							if (type == null) {
								String kfs = "";
								for (KungFuType kf : KungFuType.values()) {
									kfs = kfs + (kfs.length() > 0 ? ", " : "") + kf.toString();
								}
								sender.sendMessage(Config.message_unknown_kungfu.replace("{kungfu}", kfs));
								return true;
							}else {
								List<String> list = KungFu.getInstance().getPlayerData().getPlayerDisable(args[2]);
								if (list.contains(type.toString())) {
									list.remove(type.toString());
								}else {
									list.add(type.toString());
								}
								KungFu.getInstance().getPlayerData().setPlayerDdisable(args[2], list);
								String state = list.contains(type.toString()) ? Config.menu_item_state_disable : Config.menu_item_state_enabled;
								sender.sendMessage(Config.message_toggle_kungfu.replace("{player}", args[2]).replace("{state}", state));
								return true;
							}
						}else {
							sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_toggle);
							return true;
						}
					}else {
	        			sender.sendMessage(Config.message_no_permission);
	        			return true;
	        		}
				}
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_look);
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_set);
        		sender.sendMessage(Config.message_help_prefix + Config.message_help_kungfu_toggle);
        		return true;
    		}
    		sender.sendMessage(Config.message_unknown_command);
    		return true;
        }
        return false;
    }
}
