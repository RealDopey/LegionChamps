package net.goldenapplemc.LegionChamps;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LegionChamps extends JavaPlugin implements Listener {
	private HashMap<String, Champion> champions = new HashMap<String, Champion>();
	private BookGUI bookGUI = new BookGUI(this);
	private ChampionHealth health = new ChampionHealth(this);
	public void onEnable() { 
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(bookGUI, this);
		getServer().getPluginManager().registerEvents(health, this);
		new BukkitRunnable() {
			public void run() {
				for (Champion c : champions.values()) {
					c.saveStatsToFile();
				}
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
	
	public ChampionHealth getHealthManager() {
		return health;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Champion c = getChampion(event.getPlayer().getName());
		c.saveStatsToFile();
	}

	@EventHandler(ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String name = p.getName();
		if (!champions.containsKey(name)) {
			champions.put(name, new Champion(this, name));
		}
		if(!bookGUI.hasBook(p)) {
			bookGUI.giveBookTo(p);
		}
	}
}