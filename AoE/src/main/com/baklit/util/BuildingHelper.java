package main.com.baklit.util;

import java.util.HashMap;

import main.com.baklit.AoE;
import main.com.baklit.communication.Communications;
import main.com.baklit.types.Building;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BuildingHelper {

	static Building building;
	public static HashMap<Player, Building> playersSelectedBuilding = new HashMap<Player, Building>();

	public static Building getBuildingFromBlock(Block block) {
		building = null;
		for (Building buildings : AoE.buildings.keySet()) {

			if (buildings.containsBlock(block) == true) {
				building = buildings;
				break;
			}

		}
		return building;
	}

	public static void selectBuilding(Building building, Player player) {
		int width = building.getWidth();
		int height = building.getHeight();
		int length = building.getLength();
		Location baseLocation = building.getBottom();

		if (playersSelectedBuilding.containsKey(player)) {
			Building buildingToRemove = playersSelectedBuilding.get(player);
			deSelectBuilding(buildingToRemove, player);
		}

		for (int x = 0; x <= width;) {
			for (int z = 0; z <= length + 1;) {
				if (x == 0 || x == width) {
					Location location = new Location(player.getWorld(),
							baseLocation.getX() + 1 + x, baseLocation.getY()
									+ 1 + height, baseLocation.getZ() + z);
					player.sendBlockChange(location, Material.GOLD_BLOCK,
							(byte) 0);
				}
				if (z == 0 || z == length + 1) {
					Location location = new Location(player.getWorld(),
							baseLocation.getX() + 1 + x, baseLocation.getY()
									+ 1 + height, baseLocation.getZ() + z);
					player.sendBlockChange(location, Material.GOLD_BLOCK,
							(byte) 0);
				}
				z++;
			}
			x++;
		}
		Communications.sendMessage(Communications.SELECT_BUILDING, player,
				new NamedString("percent", building.getPercentageBuilt()),
				new NamedString("speed", building.getBuildSpeed()));
		playersSelectedBuilding.put(player, building);
	}

	public static void deSelectBuilding(Building building, Player player) {
		int width = building.getWidth();
		int height = building.getHeight();
		int length = building.getLength();
		Location baseLocation = building.getBottom();

		for (int x = 0; x <= width;) {
			for (int z = 0; z <= length + 1;) {
				if (x == 0 || x == width) {
					Location location = new Location(player.getWorld(),
							baseLocation.getX() + 1 + x, baseLocation.getY()
									+ 1 + height, baseLocation.getZ() + z);
					Block resetBlock = player.getWorld().getBlockAt(location);
					player.sendBlockChange(location, resetBlock.getType(),
							resetBlock.getData());
				}
				if (z == 0 || z == length + 1) {
					Location location = new Location(player.getWorld(),
							baseLocation.getX() + 1 + x, baseLocation.getY()
									+ 1 + height, baseLocation.getZ() + z);
					Block resetBlock = player.getWorld().getBlockAt(location);
					player.sendBlockChange(location, resetBlock.getType(),
							resetBlock.getData());
				}
				z++;
			}
			x++;
		}
		playersSelectedBuilding.remove(building);
	}
}
