package net.goldenapplemc.LegionChamps;

import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LegionChamps extends JavaPlugin implements Listener {
	private HashMap<String, Champion> champions = new HashMap<String, Champion>();

	public void onEnable() { 
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

		new BukkitRunnable() {
			public void run() {

			}
		}.runTaskTimerAsynchronously(this, 20*60, 20*60);
	}
	public void onDisable() {
		for (Champion c : champions.values()) {
			c.saveStatsToFile();
		}
	}

	public Champion getChampion(String name) {
		if (champions.containsKey(name)) {
			return (Champion)champions.get(name);
		}
		return new Champion(this, name);
	}

	@EventHandler(ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent event) {
		String name = event.getPlayer().getName();
		if (!champions.containsKey(name))
			champions.put(name, new Champion(this, name));
	}
}