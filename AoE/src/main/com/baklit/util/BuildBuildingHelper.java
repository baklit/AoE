package main.com.baklit.util;

import main.com.baklit.AoE;
import main.com.baklit.events.BuildingBuiltEvent;
import main.com.baklit.types.Building;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;

public class BuildBuildingHelper extends BukkitRunnable {

	private int height = 0;
	private int width = 0;
	private int length = 0;

	private int delaysTillBlockPlace;

	private Player player;
	private CuboidClipboard clipBoard;
	private WorldVector blockVector;
	private Building building;

	public void buildBuilding(final Player playerIn,
			final CuboidClipboard clipBoardIn, final WorldVector blockVectorIn,
			final Building buildingIn) {

		player = playerIn;
		clipBoard = clipBoardIn;
		blockVector = blockVectorIn;
		building = buildingIn;
		delaysTillBlockPlace = building.getBuildSpeed();

		this.runTaskTimer(AoE.instance, 0, building.getBuildSpeed());

	}

	public void run() {

		World world;
		Location blockLocation;
		BaseBlock testBlock;

		// if(delaysTillBlockPlace <= building.getBuildSpeed()) {
		if (delaysTillBlockPlace <= 1) {
			delaysTillBlockPlace++;
		} else {
			delaysTillBlockPlace = 0;
			world = player.getWorld();
			blockLocation = new Location(world,
					blockVector.getBlockX() + width, blockVector.getBlockY()
							+ 1 + height, blockVector.getBlockZ() + length);
			testBlock = clipBoard.getPoint(new Vector(width, height, length));
			while (testBlock.getId() == Material.AIR.getId()) {
				world.getBlockAt(blockLocation).setTypeIdAndData(
						testBlock.getId(), (byte) testBlock.getData(), false);
				building.blockSet();
				length++;
				if (length == clipBoard.getLength()) {
					length = 0;
					width++;
				}
				if (width == clipBoard.getWidth()) {
					width = 0;
					height++;
				}
				if (height == clipBoard.getHeight()) {

					Location minLoc = new Location(world,
							blockVector.getBlockX() - 1,
							blockVector.getBlockY() + 1,
							blockVector.getBlockZ() - 1);
					Location maxLoc = new Location(world,
							blockVector.getBlockX() - 1 + clipBoard.getWidth(),
							blockVector.getBlockY() + clipBoard.getHeight(),
							blockVector.getBlockZ() - 1 + clipBoard.getLength());
					BuildingBuiltEvent builtEvent = new BuildingBuiltEvent(
							minLoc, maxLoc, player);
					Bukkit.getPluginManager().callEvent(builtEvent);

					this.cancel();
					return;
				}
				blockLocation = new Location(world, blockVector.getBlockX()
						+ width, blockVector.getBlockY() + 1 + height,
						blockVector.getBlockZ() + length);
				testBlock = clipBoard
						.getPoint(new Vector(width, height, length));
			}
			world.getBlockAt(blockLocation).setTypeIdAndData(testBlock.getId(),
					(byte) testBlock.getData(), false);
			building.blockSet();

			length++;
			if (length == clipBoard.getLength()) {
				length = 0;
				width++;
			}
			if (width == clipBoard.getWidth()) {
				width = 0;
				height++;
			}
			if (height == clipBoard.getHeight()) {

				Location minLoc = new Location(world,
						blockVector.getBlockX() - 1,
						blockVector.getBlockY() + 1,
						blockVector.getBlockZ() - 1);
				Location maxLoc = new Location(world, blockVector.getBlockX()
						- 1 + clipBoard.getWidth(), blockVector.getBlockY()
						+ clipBoard.getHeight(), blockVector.getBlockZ() - 1
						+ clipBoard.getLength());
				BuildingBuiltEvent builtEvent = new BuildingBuiltEvent(minLoc,
						maxLoc, player);
				Bukkit.getPluginManager().callEvent(builtEvent);

				this.cancel();
			}
		}

	}

}
