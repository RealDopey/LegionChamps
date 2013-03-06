package net.goldenapplemc.LegionChamps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChampionHealth implements Listener {
	LegionChamps plugin;

	public ChampionHealth(LegionChamps plugin) {
		this.plugin = plugin;
		// Add regen
		startRegen();
	}

	@EventHandler
	public void onHit(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			removeHealth((Player) e.getEntity(), e.getDamage(), null);
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			removeHealth((Player) e.getEntity(), e.getDamage(), e.getDamager());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void stopFoodRegen(EntityRegainHealthEvent e) {
		if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
			e.setCancelled(true);
	}

	@Deprecated
	public void removeHealth(Player p, int amount, LivingEntity damager) {
		removeHealth(p, amount, (Entity) damager);
	}

	public void removeHealth(Player p, int amount, Entity damager) {
		Champion c = plugin.getChampion(p);
		if (damager != null) { // Null means damage done by environment, like fall. Otherwise it will be combat and needs to stop regen
			c.updateLastFight();
			if (damager instanceof Player)
				plugin.getChampion((Player) damager).updateLastFight();
		}
		c.setHp(c.getHp() - amount);
		if (c.getHp() <= 0)
			killPlayer(p, damager);
	}

	public void killPlayer(Player p, Entity killer) {
		p.setHealth(0);
		Champion c = plugin.getChampion(p);
		c.setTotalDeaths(c.getTotalDeaths() + 1);
		c.setHp(c.getMaxHp());
		if (killer == null)
			c.setDeathsToEnvironment(c.getDeathsToEnvironment() + 1);
		else if (killer instanceof Player) {
			Player killerP = (Player) killer;
			Champion killerC = plugin.getChampion(killerP);
			killerC.setPlayersKilled(killerC.getPlayersKilled() + 1);
			c.setDeathsToPlayers(c.getDeathsToPlayers() + 1);
		} else {
			c.setDeathsToMonsters(c.getDeathsToMonsters() + 1);
		}
	}

	public void startRegen() {
		final int reset = plugin.getConfig().getInt("Regen_Reset") * 1000;
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Champion c : plugin.getChampions())
					if (System.currentTimeMillis() - c.getLastFight() > reset) {
						if (c.getHp() != c.getMaxHp())
							Bukkit.getPlayer(c.name).sendMessage("Regen for you: " + c.getRegen());
						c.setHp(c.getHp() + c.getRegen()); // Add amount of regen to hp

					}

			}
		}.runTaskTimer(plugin, 10, 10); // Run every half second
	}
}
