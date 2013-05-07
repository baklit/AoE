package main.com.baklit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import main.com.baklit.communication.Communications;
import main.com.baklit.listeners.BlockListener;
import main.com.baklit.listeners.PlayerListener;
import main.com.baklit.types.Building;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AoE extends JavaPlugin {

	public static AoE instance;
	public final BlockListener blockListener;
	public final PlayerListener playerListener;
	public Map<Integer, String> schematicsList;
	public static Map<Building, Player> buildings;
	public static Map<NPC, Player> npcs;
	public static Map<Player, String> playersBuildingType;
	public File Schematics = new File("plugins/AoE/Schematics");

	public AoE() {
		blockListener = new BlockListener();
		playerListener = new PlayerListener();
		schematicsList = new HashMap<>();
		buildings = new HashMap<>();
		npcs = new HashMap<>();
		playersBuildingType = new HashMap<>();
		Communications.setPlugin(this);
	}

	@Override
	public void onDisable() {
		Communications.sendMessage(Communications.DISABLED);
	}

	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.playerListener, this);
		loadSchematics();
		Communications.sendMessage(Communications.ENABLED);
	}

	void loadSchematics() {
		schematicsList.put(schematicsList.size(), "BasicHouse");
		schematicsList.put(schematicsList.size(), "Temple-Japonais");
		for (int o = 0; o < schematicsList.size(); o++) {
			InputStream input = getClass().getResourceAsStream(
					"/resources/Schematics/" + schematicsList.get(o)
							+ ".schematic");
			if (input == null) {
				Communications.sendMessage(Communications.MISSING_SCHEMATIC,
						schematicsList.get(o));
			}
			if (input != null) {
				File outputfile = new File(Schematics.getPath() + "/"
						+ schematicsList.get(o) + ".schematic");
				Schematics.mkdirs();
				try {
					outputfile.createNewFile();
				} catch (IOException e) {
					Communications.sendMessage(Communications.OUTPUT_FILE_FAIL,
							schematicsList.get(o));
				}
				OutputStream output = null;
				try {
					output = new FileOutputStream(outputfile);
				} catch (FileNotFoundException e) {
					Communications.sendMessage(
							Communications.FAIL_TO_FIND_OUTPUT_FILE,
							schematicsList.get(o));
				}
				try {
					byte[] buffer = new byte[input.available()];
					for (int i = 0; i != -1; i = input.read(buffer)) {
						output.write(buffer, 0, i);
					}
				} catch (IOException e) {
					Communications.sendMessage(
							Communications.FAIL_TO_WRITE_SCHEMATIC,
							schematicsList.get(o));
				} finally {
					try {
						input.close();
					} catch (IOException e) {
						Communications.sendMessage(
								Communications.FAIL_TO_CLOSE_INPUT_STREAM,
								schematicsList.get(o));
					}
					try {
						output.close();
					} catch (IOException e) {
						Communications.sendMessage(
								Communications.FAIL_TO_CLOSE_OUTPUT_STREAM,
								schematicsList.get(o));
					}
				}
			}
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = Bukkit.getPlayer(sender.getName());
		if (cmd.getName().equalsIgnoreCase("aoe")) {
			if (schematicsList.containsValue(args[0])) {
				playersBuildingType.put(player, args[0]);
				return true;
			}

		}

		return false;
	}

}
