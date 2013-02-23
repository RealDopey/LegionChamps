package net.goldenapplemc.LegionChamps;

import java.lang.reflect.Field;

import net.minecraft.server.v1_4_R1.EntityPlayer;

import org.bukkit.craftbukkit.v1_4_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class ChampionHealth implements Listener {
	LegionChamps plugin;
	public ChampionHealth(LegionChamps plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void showHealthPercentage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Champion c = plugin.getChampion(p.getName());
			int hearts = (int)(20 / (c.getMaxHp() / c.getHp()));
			if(hearts == 0) hearts = 1; // Make sure they don't die unless we want them to
			p.setHealth(hearts);
			e.setDamage(0);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void stopFoodRegen(EntityRegainHealthEvent e) {
		if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
			e.setCancelled(true);
	}

	public void removeHealth(Player p, int amount, String reason, Player killer) {
		if(reason.equalsIgnoreCase("Player")) {
			setKiller(p, killer);
		}
		Champion c = plugin.getChampion(p.getName());
		int hp = c.getHp();
		c.setHp(hp - amount);
		if(c.getHp() <= 0) {
			p.setHealth(0);
			c.setHp(c.getMaxHp());
		}
	}

	public void setKiller(Player p, Player killer) {
		try {
			Field underlyingEntityField = CraftEntity.class.getDeclaredField("entity");
			underlyingEntityField.setAccessible(true);
			Object underlyingPlayerObj = underlyingEntityField.get(p);
			if(underlyingPlayerObj instanceof EntityPlayer) {
				EntityPlayer underlyingPlayer = (EntityPlayer) underlyingPlayerObj;
				underlyingPlayer.killer = (EntityPlayer) killer;
			}
		}
		catch(Exception e) {
			plugin.getLogger().severe("Reflection to set killer failed! Kill and death stats not being logged!");
		}
	}
}
