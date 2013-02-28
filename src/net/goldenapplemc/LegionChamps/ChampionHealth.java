package net.goldenapplemc.LegionChamps;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChampionHealth implements Listener {
	LegionChamps plugin;
	HashMap<String, Long> lastCombatTimes = new HashMap<String, Long>();
	public ChampionHealth(LegionChamps plugin) {
		this.plugin = plugin;
		// Add regen
		startRegen();
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			removeHealth((Player)event.getEntity(), event.getDamage(), (LivingEntity)event.getDamager());
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void stopFoodRegen(EntityRegainHealthEvent e) {
		if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
			e.setCancelled(true);
	}

	public void removeHealth(Player p, int amount, LivingEntity damager) {
		Champion c = plugin.getChampion(p.getName());
		if(damager != null) { // Null means damage done by environment, like fall. Otherwise it will be combat and needs to stop regen
			lastCombatTimes.put(c.name, System.currentTimeMillis());
			if(damager instanceof Player) {
				Player player = (Player) damager;
				lastCombatTimes.put(player.getName(), System.currentTimeMillis());
			}
		}
		c.setHp(c.getHp() - amount);
		if(c.getHp() == 0) {
			p.setHealth(1);
		}
		else {
			int hearts = (int)(20 / (c.getMaxHp() / c.getHp())); // Calculate percent of hp in hearts where 20 is max (10 hearts = 20 half hearts)
			if(hearts == 0) hearts = 1; // Make sure they don't die unless we want them to
			p.setHealth(hearts);
		}
		if(c.getHp() <= 0) {
			if(damager == null) killPlayer(p, null);
			else {
				killPlayer(p, damager); 
			}
		}
	}

	public void killPlayer(Player p, LivingEntity killer) {
		p.setHealth(0);
		Champion c = plugin.getChampion(p.getName());
		c.setTotalDeaths(c.getTotalDeaths() + 1);
		c.setHp(c.getMaxHp());
		if(killer == null) c.setDeathsToEnvironment(c.getDeathsToEnvironment() + 1);
		else if(killer instanceof Player) {
			Player killerP = (Player) killer;
			Champion killerC = plugin.getChampion(killerP.getName());
			killerC.setPlayersKilled(killerC.getPlayersKilled() + 1);
			c.setDeathsToPlayers(c.getDeathsToPlayers() + 1);
		}
		else {
			c.setDeathsToMonsters(c.getDeathsToMonsters() + 1);
		}
	}

	public void startRegen() {
		final int reset = plugin.getConfig().getInt("Regen_Reset") * 1000;
		new BukkitRunnable() {
			public void run() {
				for(Player p : plugin.getServer().getOnlinePlayers()) {
					String name = p.getName();
					Champion c = plugin.getChampion(name);
					long time = System.currentTimeMillis();
					if(lastCombatTimes.containsKey(name)) {
						long lastTime = lastCombatTimes.get(name);
						if(time - lastTime >= reset) { // Been out of combat long enough
							c.setHp(c.getHp() + c.getRegen()); // Add amount of regen to hp
						}
					}
					else {
						c.setHp(c.getHp() + c.getRegen()); // Add amount of regen to hp
					}
				}
			}
		}.runTaskTimer(plugin, 10, 10); // Run every half second
	}
}
