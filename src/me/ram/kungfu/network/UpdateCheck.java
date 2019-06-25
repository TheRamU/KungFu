package me.ram.kungfu.network;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ram.kungfu.KungFu;
import me.ram.kungfu.config.Config;
import me.ram.kungfu.utils.URLUtil;
import me.ram.kungfu.utils.UnicodeUtil;

public class UpdateCheck {
	
	private String version;
	private String post;
	private String[] update;
	
	public void check() {
		if (Config.update_check) {
			new BukkitRunnable() {
	    		@Override
	            public void run() {
	    			String document = URLUtil.getDocumentAt("https://raw.githubusercontent.com/13527698822/UpdateCheck/master/KungFu.yml");
	    			if (document != null && !document.equals("")) {
	    				String[] info = document.split(",");
	    				version = info[0];
	    				post = info[1];
	    				update = info[2].split(";");
	    				if (!version.equals(KungFu.getVersion())) {
	    					sendInfo(Bukkit.getConsoleSender());
	    				}
	    			}
	    		}
			}.runTaskLaterAsynchronously(KungFu.getInstance(), 5L);
			new BukkitRunnable() {
	    		@Override
	            public void run() {
	    			String document = URLUtil.getDocumentAt("https://raw.githubusercontent.com/13527698822/UpdateCheck/master/KungFu.yml");
	    			if (document != null && !document.equals("")) {
	    				String[] info = document.split(",");
	    				version = info[0];
	    				post = info[1];
	    				update = info[2].split(";");
	    			}
	    		}
			}.runTaskTimerAsynchronously(KungFu.getInstance(), 72000L, 72000L);
		}
	}
	public void playerJoin(Player player) {
		if (Config.update_check && version != null && post != null && update != null) {
			if (player.hasPermission("kungfu.updatecheck") && !version.equals(KungFu.getVersion())) {
				sendInfo(player);
			}	
		}
	}
	private void sendInfo(CommandSender sender) {
		new BukkitRunnable() {
    		@Override
            public void run() {
    			sender.sendMessage("");
    			sender.sendMessage("§b§lKungFu §f>> §f检测到一个可用的版本更新！");
    			sender.sendMessage("   §f当前版本: §a" + KungFu.getVersion());
    			sender.sendMessage("   §f更新版本: §a" + version);
    			sender.sendMessage("   §f更新内容: ");
    			for (int i = 0; i < update.length; i++) {
    				sender.sendMessage("     §f" + (i + 1) + ".§e" + UnicodeUtil.unicodeToCn(update[i]));
    			}
    			sender.sendMessage("   §f更新地址: §b§n" + post);
    		}
		}.runTaskLaterAsynchronously(KungFu.getInstance(), 5L);
	}
}
