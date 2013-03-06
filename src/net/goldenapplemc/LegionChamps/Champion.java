package net.goldenapplemc.LegionChamps;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Champion {
	LegionChamps plugin;
	String name;
	FileConfiguration config;
	private int strength = 0;
	private int agility = 0;
	private int endurance = 0;
	private int precision = 0;
	private int hp = 0;
	private int maxHp = 0;
	private int regen = 0;
	private int level = 0;
	private int exp = 0;
	private double crit = 0;
	private double dodge = 0;
	private double itemFind = 0;
	private double goldFind = 0;

	private int gold = 0;
	private int damageDone = 0;
	private int damageTaken = 0;
	private int expGained = 0;
	private int playersKilled = 0;
	private int killStreaks = 0;
	private int highestStreak = 0;
	private int highestMultikill = 0;
	private int totalGold = 0;
	private int goldFromMonsters = 0;
	private int goldFromShops = 0;
	private int goldFromTrade = 0;
	private int goldSpent = 0;
	private int totalDeaths = 0;
	private int deathsToPlayers = 0;
	private int deathsToMonsters = 0;
	private int deathsToEnvironment = 0;
	private int mobsKilled = 0;
	private int bossesKilled = 0;

	private long lastFight;

	private File file;

	public Champion(LegionChamps plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		config = plugin.getConfig();
		file = new File(plugin.getDataFolder(), "Champions/" + name + ".yml");
		createFileIfAbsent();
		loadStatsFromFile();
	}

	public void createFileIfAbsent() {
		try {
			if (!file.createNewFile())
				return;
			FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection defaults = config.getConfigurationSection("Default_Stats");
			String[] toCopy = new String[] { "Strength", "Agility", "Endurance", "Precision", "Health", "MaxHealth", "Regen", "Level", "Exp", "CritChance",
					"DodgeChance", "ItemFind", "GoldFind", "DamageDone", "DamageTaken", "ExpGained", "PlayersKilled", "KillStreaks", "HighestKillStreak",
					"TotalGold", "GoldFromMonsters", "GoldFromShops", "GoldFromTrade", "GoldSpent", "TotalDeaths", "DeathsToPlayers", "DeathsToMonsters",
					"DeathsToEnvironment", "TotalMobsKilled", "BossesKilled" };
			for (String att : toCopy) {
				yml.set(att, defaults.get(att, 0));
			}

			yml.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Error creating Champion file for: " + name);
			e.printStackTrace();
		}
	}

	public void saveStatsToFile() {
		FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
		yml.set("Strength", strength);
		yml.set("Agility", agility);
		yml.set("Endurance", endurance);
		yml.set("Precision", precision);
		yml.set("Health", hp);
		yml.set("MaxHealth", maxHp);
		yml.set("Regen", regen);
		yml.set("Level", level);
		yml.set("Exp", exp);
		yml.set("CritChance", crit);
		yml.set("DodgeChance", dodge);
		yml.set("ItemFind", itemFind);
		yml.set("GoldFind", goldFind);

		yml.set("DamageDone", damageDone);
		yml.set("DamageTaken", damageTaken);
		yml.set("ExpGained", expGained);
		yml.set("PlayersKilled", playersKilled);
		yml.set("KillStreaks", killStreaks);
		yml.set("HighestKillStreak", highestStreak);
		yml.set("TotalGold", totalGold);
		yml.set("GoldFromMonsters", goldFromMonsters);
		yml.set("GoldFromShops", goldFromShops);
		yml.set("GoldFromTrade", goldFromTrade);
		yml.set("GoldSpent", goldSpent);
		yml.set("TotalDeaths", totalDeaths);
		yml.set("DeathsToPlayers", deathsToPlayers);
		yml.set("DeathsToMonsters", deathsToMonsters);
		yml.set("DeathsToEnvironment", deathsToEnvironment);
		yml.set("TotalMobsKilled", mobsKilled);
		yml.set("BossesKilled", bossesKilled);
		try {
			yml.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Error saving Champion file for: " + name);
			e.printStackTrace();
		}
	}

	public void loadStatsFromFile() {
		FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
		strength = yml.getInt("Strength");
		agility = yml.getInt("Agility");
		endurance = yml.getInt("Endurance");
		precision = yml.getInt("Precision");
		hp = yml.getInt("Health");
		maxHp = yml.getInt("MaxHealth");
		regen = yml.getInt("Regen");
		level = yml.getInt("Level");
		exp = yml.getInt("Exp");
		crit = yml.getDouble("CritChance");
		dodge = yml.getDouble("DodgeChance");
		itemFind = yml.getDouble("ItemFind");
		goldFind = yml.getDouble("GoldFind");

		damageDone = yml.getInt("DamageDone");
		damageTaken = yml.getInt("DamageTaken");
		expGained = yml.getInt("ExpGained");
		playersKilled = yml.getInt("PlayersKilled");
		killStreaks = yml.getInt("KillStreaks");
		highestStreak = yml.getInt("HighestKillStreak");
		totalGold = yml.getInt("TotalGold");
		goldFromMonsters = yml.getInt("GoldFromMonsters");
		goldFromShops = yml.getInt("GoldFromShops");
		goldFromTrade = yml.getInt("GoldFromTrade");
		goldSpent = yml.getInt("GoldSpent");
		totalDeaths = yml.getInt("TotalDeaths");
		deathsToPlayers = yml.getInt("DeathsToPlayers");
		deathsToMonsters = yml.getInt("DeathsToMonsters");
		deathsToEnvironment = yml.getInt("DeathsToEnvironment");
		mobsKilled = yml.getInt("TotalMobsKilled");
		bossesKilled = yml.getInt("BossesKilled");
		updateBookInInv();
	}

	public void updateBookInInv() {
		OfflinePlayer p = plugin.getServer().getOfflinePlayer(name);
		if (p.isOnline()) {
			if (!plugin.getBookManager().hasBook((Player) p)) {
				plugin.getBookManager().giveBookTo((Player) p);
			}
			ItemStack i = plugin.getBookManager().getBook((Player) p);
			i.setItemMeta(plugin.getBookManager().getBookMeta(this, i));
		}
	}

	public int getStrength() {
		return strength;
	}

	public int getAgility() {
		return agility;
	}

	public int getEndurance() {
		return endurance;
	}

	public int getPrecision() {
		return precision;
	}

	public int getHp() {
		return hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getRegen() {
		return regen;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getExpRequiredForCurrentLevel() {
		return 0;
	}

	public double getCritChance() {
		return crit;
	}

	public double getDodgeChance() {
		return dodge;
	}

	public double getItemFind() {
		return itemFind;
	}

	public double getGoldFind() {
		return goldFind;
	}

	public void setStrength(int value) {
		strength = value;
		updateBookInInv();
	}

	public void setAgility(int value) {
		agility = value;
		updateBookInInv();
	}

	public void setEndurance(int value) {
		endurance = value;
		updateBookInInv();
	}

	public void setPrecision(int value) {
		precision = value;
		updateBookInInv();
	}

	public void setHp(int value) {
		if (value > getMaxHp())
			value = getMaxHp(); // Don't let hp go above max
		hp = value;

		Bukkit.getPlayer(name).setHealth(Math.max(1, (int) (20 * (hp / (getMaxHp() * 1.0d)))));

		updateBookInInv();
	}

	public void setMaxHp(int value) {
		maxHp = value;
		updateBookInInv();
	}

	public void setRegen(int value) {
		regen = value;
		updateBookInInv();
	}

	public void setLevel(int value) {
		if (value > 200)
			value = 200;
		level = value;
		updateBookInInv();
	}

	public void setExp(int value) {
		exp = value;
		updateBookInInv();
	}

	public void setCritChance(double value) {
		crit = value;
		updateBookInInv();
	}

	public void setDodgeChance(double value) {
		dodge = value;
		updateBookInInv();
	}

	public void setItemFind(double value) {
		itemFind = value;
		updateBookInInv();
	}

	public void setGoldFind(double value) {
		goldFind = value;
		updateBookInInv();
	}

	public int getGold() {
		return gold;
	}

	public int getDamageDone() {
		return damageDone;
	}

	public int getDamageTaken() {
		return damageTaken;
	}

	public int getTotalExpEarned() {
		return expGained;
	}

	public int getPlayersKilled() {
		return playersKilled;
	}

	public int getKillStreaks() {
		return killStreaks;
	}

	public int getHighestStreak() {
		return highestStreak;
	}

	public int getHighestMultikill() {
		return highestMultikill;
	}

	public int getTotalGoldEarned() {
		return totalGold;
	}

	public int getGoldEarnedFromMonsters() {
		return goldFromMonsters;
	}

	public int getGoldEarnedFromShops() {
		return goldFromShops;
	}

	public int getGoldEarnedFromTrade() {
		return goldFromTrade;
	}

	public int getTotalGoldSpent() {
		return goldSpent;
	}

	public int getTotalDeaths() {
		return totalDeaths;
	}

	public int getDeathsToPlayers() {
		return deathsToPlayers;
	}

	public int getDeathsToMonsters() {
		return deathsToMonsters;
	}

	public int getDeathsToEnvironment() {
		return deathsToEnvironment;
	}

	public int getTotalMobsKilled() {
		return mobsKilled;
	}

	public int getBossesKilled() {
		return bossesKilled;
	}

	public void setDamageDone(int value) {
		damageDone = value;
		updateBookInInv();
	}

	public void setDamageTaken(int value) {
		damageTaken = value;
		updateBookInInv();
	}

	public void setTotalExpGained(int value) {
		expGained = value;
		updateBookInInv();
	}

	public void setPlayersKilled(int value) {
		playersKilled = value;
		updateBookInInv();
	}

	public void setNumberOfStreaks(int value) {
		killStreaks = value;
		updateBookInInv();
	}

	public void setHighestStreak(int value) {
		highestStreak = value;
		updateBookInInv();
	}

	public void setHighestMultikill(int value) {
		highestMultikill = value;
		updateBookInInv();
	}

	public void setTotalGoldEarned(int value) {
		totalGold = value;
		updateBookInInv();
	}

	public void setGoldFromMonsters(int value) {
		goldFromMonsters = value;
		updateBookInInv();
	}

	public void setGoldFromShops(int value) {
		goldFromShops = value;
		updateBookInInv();
	}

	public void setGoldFromTrade(int value) {
		goldFromTrade = value;
		updateBookInInv();
	}

	public void setGoldSpent(int value) {
		goldSpent = value;
		updateBookInInv();
	}

	public void setTotalDeaths(int value) {
		totalDeaths = value;
		updateBookInInv();
	}

	public void setDeathsToPlayers(int value) {
		deathsToPlayers = value;
		updateBookInInv();
	}

	public void setDeathsToMonsters(int value) {
		deathsToMonsters = value;
		updateBookInInv();
	}

	public void setDeathsToEnvironment(int value) {
		deathsToEnvironment = value;
		updateBookInInv();
	}

	public void setTotalMobsKilled(int value) {
		mobsKilled = value;
		updateBookInInv();
	}

	public void setBossesKilled(int value) {
		bossesKilled = value;
		updateBookInInv();
	}

	public long getLastFight() {
		return lastFight;
	}

	public void updateLastFight() {
		lastFight = System.currentTimeMillis();
	}

}