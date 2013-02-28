package net.goldenapplemc.LegionChamps;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class BookGUI implements Listener {
	LegionChamps plugin;
	public BookGUI(LegionChamps plugin) {
		this.plugin = plugin;
	}

	public boolean hasBook(Player p) {
		boolean hasBook = false;
		for(ItemStack i : p.getInventory().getContents()) {
			if(i != null) {
				if(i.getType() == Material.WRITTEN_BOOK) {
					if(i.hasItemMeta()) {
						BookMeta book = (BookMeta) i.getItemMeta();
						if(ChatColor.stripColor(book.getTitle()).equalsIgnoreCase("Champion Info")) {
							hasBook = true;
							break;
						}
					}
				}
			}
		}
		return hasBook;
	}

	public ItemStack getBook(Player p) {
		for(ItemStack i : ((Player) p).getInventory().getContents()) {
			if(i.getType() == Material.WRITTEN_BOOK) {
				BookMeta meta = (BookMeta) i.getItemMeta();
				if(ChatColor.stripColor(meta.getTitle()).equalsIgnoreCase("Champion Info")) {
					return i;
				}
			}
		}
		return null;
	}

	public void giveBookTo(Player p) {
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta book = (BookMeta) bookItem.getItemMeta();
		book.setTitle(ChatColor.LIGHT_PURPLE + "Champion Info");
		book.setAuthor("The Legion Master");
		book.addPage("");
		book.addPage("");
		book.addPage("");
		bookItem.setItemMeta(book);
		bookItem.setItemMeta(getBookMeta(plugin.getChampion(p.getName()), bookItem));
		p.getInventory().addItem(bookItem);
	}

	public BookMeta getBookMeta(Champion c, ItemStack bookItem) {
		BookMeta book = (BookMeta) bookItem.getItemMeta();
		book.setTitle(ChatColor.LIGHT_PURPLE + "Champion Info");
		book.setAuthor("The Legion Master");
		book.setPage(1, " " + ChatColor.UNDERLINE + ChatColor.BOLD + "CHAMPION PROFILE" + ChatColor.RESET + " \n\n" +
				ChatColor.BOLD + "Name: " + ChatColor.RESET + c.name + "\n" +
				ChatColor.ITALIC + "Title\n" + ChatColor.RESET +
				ChatColor.BOLD + "Level " + ChatColor.RESET + c.getLevel() + "\n" +
				" ----------------- " + "\n" +
				ChatColor.BOLD + "HP: " + ChatColor.RESET + c.getHp() + "/" + c.getMaxHp() + "\n" +
				ChatColor.BOLD + "Regen: " + ChatColor.RESET + c.getRegen() * 2 + "/s\n\n" +
				ChatColor.BOLD + "STR: " + ChatColor.RESET + c.getStrength() + ChatColor.BOLD + "      PRE: " + ChatColor.RESET + c.getPrecision() + "\n" +
				ChatColor.BOLD + "AGI: " + ChatColor.RESET + c.getAgility() + ChatColor.BOLD + "      END: " + ChatColor.RESET + c.getEndurance() +
				" ----------------- " +
				ChatColor.BOLD + "Gold: " + ChatColor.RESET + c.getGold());

		book.setPage(2, "       " + ChatColor.BOLD + "PvE Stats" + ChatColor.RESET + "   \n\n" +
				ChatColor.BOLD + "Total Gold: " + ChatColor.RESET + c.getTotalGoldEarned() + "\n\n" +
				ChatColor.BOLD + "Gold Spent: " + ChatColor.RESET + c.getTotalGoldSpent() + "\n\n" +
				ChatColor.BOLD + "Monsters Killed: " + ChatColor.RESET + c.getTotalMobsKilled() + "\n\n" +
				ChatColor.BOLD + "Bosses Killed: " + ChatColor.RESET + c.getBossesKilled() + "\n\n" +
				ChatColor.BOLD + "Raids Killed: " + ChatColor.RESET + 0 + "\n\n" +
				ChatColor.BOLD + "Damage Dealt: " + ChatColor.RESET + c.getDamageDone() + "\n\n" +
				ChatColor.BOLD + "Damage Taken: " + ChatColor.RESET + c.getDamageTaken() + "\n\n");
		book.setPage(3, "" + ChatColor.BOLD + ChatColor.RED + "          PvP Stats" + ChatColor.RESET + "   \n" +
				ChatColor.RED + "Kills: " + c.getPlayersKilled() + "    Deaths: " + c.getTotalDeaths() + "\n" +
				"Rank: " + 0 + "    ELO: " + 0 + "\n" +
				"Max Multi: " + c.getHighestMultikill() + "\n" +
				"Max Streak: " + c.getHighestStreak());
		return book;
	}
	// DOESNT WORK WTF BRUH
	public String centerLine(String line) {
		int used = 0;
		for(int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if(Character.isSpaceChar(c)) {
				used = used + 19;
			}
			else {
				used = used + 27;
			}
		}
		System.out.println("Used: " + used);
		int remaining = 513 - used;
		System.out.println("Remaining: " + remaining);
		int spacesPerSide = (remaining / 19) / 2;
		System.out.println(spacesPerSide);
		while(spacesPerSide > 0) {
			line = " " + line + " ";
			spacesPerSide--;
		}
		System.out.println(line);
		return line;
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().getType() == Material.WRITTEN_BOOK) {
			BookMeta book = (BookMeta) event.getItemDrop().getItemStack().getItemMeta();
			if(ChatColor.stripColor(book.getTitle()).equalsIgnoreCase("Champion Info")) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to drop this item!");
			}
		}
	}

	@EventHandler
	public void onItemSelect(InventoryClickEvent event) {
		if(event.getCurrentItem() != null) {
			if(event.getCurrentItem().getType() == Material.WRITTEN_BOOK) {
				BookMeta book = (BookMeta) event.getCurrentItem().getItemMeta();
				if(ChatColor.stripColor(book.getTitle()).equalsIgnoreCase("Champion Info")) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		ItemStack i = null;
		for(ItemStack drop : event.getDrops()) {
			if(drop.getType() == Material.WRITTEN_BOOK) {
				BookMeta book = (BookMeta) drop.getItemMeta();
				if(ChatColor.stripColor(book.getTitle()).equalsIgnoreCase("Champion Info")) {
					i = drop;
				}
			}
		}
		event.getDrops().remove(i);
	}

	@EventHandler
	public void onRespawn(final PlayerRespawnEvent event) {
		new BukkitRunnable() {
			public void run() {
				if(!hasBook(event.getPlayer())) {
					giveBookTo(event.getPlayer());
				}
			}
		}.runTaskLater(plugin, 3);
	}
}
