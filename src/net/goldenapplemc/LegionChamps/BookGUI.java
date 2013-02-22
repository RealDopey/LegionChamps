package net.goldenapplemc.LegionChamps;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

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
	
	public void giveBookTo(Player p) {
		Champion c = plugin.getChampion(p.getName());
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta book = (BookMeta) bookItem.getItemMeta();
		book.setTitle(ChatColor.LIGHT_PURPLE + "Champion Info");
		book.setAuthor("The Legion Master");
		// 26 spaces to a line
		book.addPage("     " + ChatColor.UNDERLINE + "Your Champion\n\n" + ChatColor.RESET +
		"     Health: " + c.getHp() + "/" + c.getMaxHp() + "\n\n" +
		"     Regen: " + c.getRegen() + " HP/sec\n\n" +
		"     Level: " + c.getLevel() + "\n\n" +
		"     Exp: " + c.getExp() + "\n\n" +
		"     Exp Needed: " + c.getExpRequiredForCurrentLevel() + "\n\n" +
		centerLine("This is a test"));
		bookItem.setItemMeta(book);
		p.getInventory().addItem(bookItem);
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
}
