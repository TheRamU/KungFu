package me.ram.kungfu.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class SoundUtil {
	
    public static Sound getSound(String oldsound, String newsound) {
		String name = Bukkit.getServer().getClass().getPackage().getName();
        name = name.substring(name.lastIndexOf(46) + 1);
        try {
            if (name.startsWith("v1_8") || name.startsWith("v1_7")) {
            	return Sound.valueOf(oldsound);
            }else {
            	return Sound.valueOf(newsound);
            }
        } catch (Exception ex) {
        	return null;
        }
    }
    
}
