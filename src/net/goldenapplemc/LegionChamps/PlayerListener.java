package net.goldenapplemc.LegionChamps;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {
	LegionChamps plugin;

	public PlayerListener(LegionChamps plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player died = event.getEntity();
		Champion diedC = this.plugin.getChampion(died.getName());
		diedC.setTotalDeaths(diedC.getTotalDeaths() + 1);
		if (event.getEntity().getKiller() != null) {
			if ((event.getEntity().getKiller() instanceof Player)) {
				Player p = event.getEntity().getKiller();
				Champion c = this.plugin.getChampion(p.getName());
				c.setPlayersKilled(c.getPlayersKilled() + 1);

				diedC.setDeathsToPlayers(diedC.getDeathsToPlayers() + 1);
			}

			if ((event.getEntity().getKiller() instanceof Projectile)) {
				Projectile projectile = (Projectile)event.getEntity().getKiller();
				if ((projectile.getShooter() instanceof Player)) {
					Player p = (Player)projectile.getShooter();
					Champion c = this.plugin.getChampion(p.getName());
					c.setPlayersKilled(c.getPlayersKilled() + 1);

					diedC.setDeathsToPlayers(diedC.getDeathsToPlayers() + 1);
				}
				else {
					diedC.setDeathsToMonsters(diedC.getDeathsToMonsters() + 1);
				}
			}
		}
		else {
			diedC.setDeathsToEnvironment(diedC.getDeathsToEnvironment() + 1);
		}
	}
}