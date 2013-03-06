package net.goldenapplemc.LegionChamps;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LegionChamps extends JavaPlugin implements Listener {
	private ChampionStore champions;
	private BookGUI bookGUI = new BookGUI(this);
	private ChampionHealth health;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		health = new ChampionHealth(this);
		champions = new ChampionStore(this);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(bookGUI, this);
		getServer().getPluginManager().registerEvents(health, this);

	}

	@Override
	public void onDisable() {
		champions.saveAll();
	}

	public Champion getChampion(Player p) {
		return champions.get(p);
	}

	@Deprecated
	public Champion getChampion(String s) {
		return champions.get(Bukkit.getPlayer(s));
	}

	public Collection<Champion> getChampions() {
		return champions.getAll();
	}

	public ChampionHealth getHealthManager() {
		return health;
	}

	public BookGUI getBookManager() {
		return bookGUI;
	}

	@EventHandler(ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event) {
		champions.get(event.getPlayer()).updateBookInInv();
	}
}