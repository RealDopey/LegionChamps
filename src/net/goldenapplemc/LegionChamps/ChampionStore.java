package net.goldenapplemc.LegionChamps;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

public class ChampionStore implements Listener {
	private final LegionChamps plugin;
	private final Map<String, Champion> data = Maps.newHashMap();

	public ChampionStore(LegionChamps plugin) {
		this.plugin = plugin;
		plugin.getLogger().fine("Creating champ store");
		Bukkit.getPluginManager().registerEvents(this, plugin);

		new BukkitRunnable() {
			@Override
			public void run() {
				saveAll();
			}
		}.runTaskTimer(plugin, 20 * 60, 20 * 60);
		new File(plugin.getDataFolder(), "Champions").mkdirs();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogout(PlayerQuitEvent e) {
		data.get(e.getPlayer().getName()).saveStatsToFile();
		data.remove(e.getPlayer().getName());
	}

	public void saveAll() {
		plugin.getLogger().fine("Saving " + data.size() + " champs.");
		for (Champion c : data.values())
			c.saveStatsToFile();
	}

	public Champion get(Player p) {
		Champion c = data.get(p.getName());
		if (c == null) {
			c = new Champion(plugin, p.getName());
			data.put(c.name, c);
		}
		return c;
	}

	public Collection<Champion> getAll() {
		return Collections.unmodifiableCollection(data.values());
	}
}
