package main.com.baklit.communication;

import java.util.logging.Level;

import main.com.baklit.util.NamedString;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Communications {
	private static final String MESSAGES_PREFIX = "messages.";
	private static final String PLAYER_MESSAGES_PREFIX = MESSAGES_PREFIX
			+ "player.";
	public static final String DISABLED = "disabled";
	public static final String ENABLED = "enabled";
	public static final String MISSING_SCHEMATIC = "missing-schematic";
	public static final String OUTPUT_FILE_FAIL = "fail-to-make-output-file";
	public static final String FAIL_TO_FIND_OUTPUT_FILE = "fail-to-find-output-file";
	public static final String FAIL_TO_WRITE_SCHEMATIC = "fail-to-write-schematic";
	public static final String FAIL_TO_CLOSE_INPUT_STREAM = "fail-to-close-input-stream";
	public static final String FAIL_TO_CLOSE_OUTPUT_STREAM = "fail-to-close-output-stream";
	public static final String VILLAGER_ATTACHED = "villager-attached";

	public static final String PLAYER_FAIL_TO_LOAD_SCHEMATIC = "fail-to-load-schematic";
	public static final String NOT_A_BUILDING = "not-a-building";
	public static final String SELECT_A_BUILDING_FIRST = "select-a-building";
	public static final String SELECT_BUILDING = "select-building";
	public static final String BUILDING_ID_AND_NAME = "building-id-and-name";
	public static final String BUILDING_DISTANCE_AWAY = "building-distance";
	private static JavaPlugin plugin;

	public static void sendMessage(String configMessage, Object... other) {
		String realMessage = plugin.getConfig().getString(
				MESSAGES_PREFIX + configMessage);
		realMessage = realMessage.replace("\\u00A7", "\u00A7");

		Level level = Level.INFO;
		if (realMessage.startsWith("fine")) {
			realMessage = realMessage.substring(4);
			level = Level.FINE;
		} else if (realMessage.startsWith("warn")) {
			realMessage = realMessage.substring(4);
			level = Level.WARNING;
		} else if (realMessage.startsWith("severe")) {
			realMessage = realMessage.substring(6);
			level = Level.SEVERE;
		}
		ChatColor.stripColor(realMessage);
		realMessage = replaceObjects(realMessage, other);
		plugin.getLogger().log(level, realMessage);

	}

	public static void sendMessage(String configMessage, CommandSender sender,
			Object... other) {
		String realMessage = plugin.getConfig().getString(
				PLAYER_MESSAGES_PREFIX + configMessage);
		realMessage = realMessage.replace("\\u00A7", "\u00A7");
		realMessage = replaceObjects(realMessage, other);
		sender.sendMessage(realMessage);
	}

	public static void setPlugin(JavaPlugin plugin) {
		Communications.plugin = plugin;
	}

	private static String replaceObjects(String realMessage, Object[] other) {
		for (Object o : other) {
			if (o instanceof Player) {
				realMessage = realMessage.replace("<player>",
						((Player) o).getName());
			} else if (o instanceof NamedString) {
				NamedString nn = (NamedString) o;
				realMessage = realMessage.replace("<" + nn.getName() + ">",
						nn.getValue());
				System.out.println("replaced " + nn.getName() + " with "
						+ nn.getValue());
			} else {
				realMessage.replace("<" + o.getClass().getSimpleName() + ">",
						o.toString());
			}
		}
		return realMessage;
	}
}
