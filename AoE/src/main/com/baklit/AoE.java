package main.com.baklit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import main.com.baklit.listeners.BlockListener;
import main.com.baklit.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AoE extends JavaPlugin{
	
	public static AoE instance;
	public final BlockListener blocklistener  = new BlockListener();
	public final PlayerListener playerlistener  = new PlayerListener();
	public HashMap<Integer, String> SchematicsList = new HashMap<Integer, String>();
	public File Schematics = new File("plugins/AoE/Schematics");
	
	@Override
	public void onDisable(){
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Age of Empires has been disabled.");
	}
	
	@Override
	public void onEnable(){
		
		instance = this;
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this.blocklistener, this);
		pm.registerEvents(this.playerlistener, this);
		loadSchematics();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Age of Empires has been enabled.");
	}
	
	void loadSchematics(){
		SchematicsList.put(SchematicsList.size(),"BasicHouse");
		SchematicsList.put(SchematicsList.size(),"BasicHousValid");
		SchematicsList.put(SchematicsList.size(),"BasicHouseInvalid");
		for (int o=0;o<SchematicsList.size();o++){
		InputStream input = getClass().getResourceAsStream("/resources/Schematics/" + SchematicsList.get(o) + ".schematic");
		if (input == null){	
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to locate schematic " + SchematicsList.get(o) + ". Try server restart (/reload will not suffice).");
		}
		if (input != null){
		File outputfile = new File(Schematics.getPath() + "/" + SchematicsList.get(o) + ".schematic");
		Schematics.mkdirs();
		try {
			outputfile.createNewFile();
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to create output file.");
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(outputfile);
		} catch (FileNotFoundException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to locate output file location.");
		}
		try {
		    byte[] buffer = new byte[input.available()];
		    for (int i = 0; i != -1; i = input.read(buffer)) {
		        output.write(buffer, 0, i);
		    }
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to write schematics.");
		} finally {
		    try {
				input.close();
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to close input stream.");
			}
		    try {
				output.close();
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[AoE] Failed to close output stream.");
			}
		}
		}
		}
	}

}
