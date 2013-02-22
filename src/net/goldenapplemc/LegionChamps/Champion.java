package net.goldenapplemc.LegionChamps;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
	private int expGained = 0;
	private int playersKilled = 0;
	private int killStreaks = 0;
	private int highestStreak = 0;
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
	private HashMap<String, Integer> mobTypesKilled = new HashMap<String, Integer>();
	private int bossesKilled = 0;

	public Champion(LegionChamps plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		config = plugin.getConfig();
		loadStatsFromFile();
	}

	public boolean hasFile() {
		File file = new File("plugins/LegionChamps/Champions/" + name + ".yml");
		if (file.exists()) return true;
		return false;
	}

	public void createFile() {
		if (hasFile()) return;
		File file = new File("plugins/LegionChamps/Champions/" + name + ".yml");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection stats = config.getConfigurationSection("Default_Stats");
			yml.set("Strength", stats.getInt("Strength"));
			yml.set("Agility", stats.getInt("Agility"));
			yml.set("Endurance", stats.getInt("Endurance"));
			yml.set("Precision", stats.getInt("Precision"));
			yml.set("Health", stats.getInt("Health"));
			yml.set("MaxHealth", stats.getInt("MaxHealth"));
			yml.set("Regen", stats.getInt("Regen"));
			yml.set("Level", 0);
			yml.set("Exp", 0);
			yml.set("CritChance", stats.getDouble("CritChance"));
			yml.set("DodgeChance", stats.getDouble("DodgeChance"));
			yml.set("ItemFind", stats.getDouble("ItemFind"));
			yml.set("GoldFind", stats.getDouble("GoldFind"));

			yml.set("DamageDone", 0);
			yml.set("ExpGained", 0);
			yml.set("PlayersKilled", 0);
			yml.set("KillStreaks", 0);
			yml.set("HighestKillStreak", 0);
			yml.set("TotalGold", 0);
			yml.set("GoldFromMonsters", 0);
			yml.set("GoldFromShops", 0);
			yml.set("GoldFromTrade", 0);
			yml.set("GoldSpent", 0);
			yml.set("TotalDeaths", 0);
			yml.set("DeathsToPlayers", 0);
			yml.set("DeathsToMonsters", 0);
			yml.set("DeathsToEnvironment", 0);
			yml.set("TotalMobsKilled", 0);
			yml.set("BossesKilled", 0);
			yml.set("MobTypesKilled", "none");
			yml.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Error creating Champion file for: " + name);
			e.printStackTrace();
		}
	}

	public void saveStatsToFile() {
		if (!hasFile()) createFile();
		File file = new File("plugins/LegionChamps/Champions/" + name + ".yml");
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
		for (String s : mobTypesKilled.keySet()) {
			yml.set("MobTypesKilled." + s, mobTypesKilled.get(s));
		}
		if (mobTypesKilled.isEmpty()) yml.set("MobTypesKilled", "none"); 
		try {
			yml.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Error saving Champion file for: " + name);
			e.printStackTrace();
		}
	}

	public void loadStatsFromFile() {
		if (!hasFile()) createFile();
		File file = new File("plugins/LegionChamps/Champions/" + name + ".yml");
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
		crit = yml.getInt("CritChance");
		dodge = yml.getInt("DodgeChance");
		itemFind = yml.getInt("ItemFind");
		goldFind = yml.getInt("GoldFind");
		
		damageDone = yml.getInt("DamageDone");
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
/*		if (!(yml.get("MobTypesKilled") instanceof String)) {
			for (String s : yml.getConfigurationSection("MobTypesKilled").getKeys(false)) {
				mobTypesKilled.put(s, yml.getInt("MobTypesKilled." + s));
			}
		}
		else {
			mobTypesKilled.clear();
		} */
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
	}

	public void setAgility(int value) {
		agility = value;
	}

	public void setEndurance(int value) {
		endurance = value;
	}

	public void setPrecision(int value) {
		precision = value;
	}

	public void setHp(int value) {
		hp = value;
	}

	public void setMaxHp(int value) {
		maxHp = value;
	}

	public void setRegen(int value) {
		regen = value;
	}

	public void setLevel(int value) {
		if(value > 200) value = 200;
		level = value;
	}

	public void setExp(int value) {
		exp = value;
	}

	public void setCritChance(double value) {
		crit = value;
	}

	public void setDodgeChance(double value) {
		dodge = value;
	}

	public void setItemFind(double value) {
		itemFind = value;
	}

	public void setGoldFind(double value) {
		goldFind = value;
	}

	public int getGold()
	{
		return gold;
	}

	public int getDamageDone() {
		return damageDone;
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

	public HashMap<String, Integer> getMobTypesKilled() {
		return mobTypesKilled;
	}

	public int getBossesKilled() {
		return bossesKilled;
	}

	public void setDamageDone(int value) {
		damageDone = value;
	}

	public void setTotalExpGained(int value) {
		expGained = value;
	}

	public void setPlayersKilled(int value) {
		playersKilled = value;
	}

	public void setNumberOfStreaks(int value) {
		killStreaks = value;
	}

	public void setHighestStreak(int value) {
		highestStreak = value;
	}

	public void setTotalGoldEarned(int value) {
		totalGold = value;
	}

	public void setGoldFromMonsters(int value) {
		goldFromMonsters = value;
	}

	public void setGoldFromShops(int value) {
		goldFromShops = value;
	}

	public void setGoldFromTrade(int value) {
		goldFromTrade = value;
	}

	public void setGoldSpent(int value) {
		goldSpent = value;
	}

	public void setTotalDeaths(int value) {
		totalDeaths = value;
	}

	public void setDeathsToPlayers(int value) {
		deathsToPlayers = value;
	}

	public void setDeathsToMonsters(int value) {
		deathsToMonsters = value;
	}

	public void setDeathsToEnvironment(int value) {
		deathsToEnvironment = value;
	}

	public void setTotalMobsKilled(int value) {
		mobsKilled = value;
	}

	public void setMobTypesKilled(HashMap<String, Integer> value) {
		mobTypesKilled = value;
	}

	public void setBossesKilled(int value) {
		bossesKilled = value;
	}
}