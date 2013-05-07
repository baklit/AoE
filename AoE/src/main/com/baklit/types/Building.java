package main.com.baklit.types;

import main.com.baklit.AoE;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;

public class Building {
	private Location bottom;
	private Location top;
	private Player owner;
	private World world;
	private Vector size;
	private int buildSpeed;
	private int totalBlocks;
	private int setBlocks;
	private double percentageBuilt;
	private int width;
	private int height;
	private int length;
	private int id;

	public Building(Location min, Location max, Player ownerIn, String typeIn) {
		id = AoE.buildings.size();

		bottom = min;
		top = max;
		owner = ownerIn;
		world = bottom.getWorld();

		width = top.getBlockX() - bottom.getBlockX();
		height = top.getBlockY() - bottom.getBlockY();
		length = top.getBlockZ() - bottom.getBlockZ();

		totalBlocks = width * height * length;

		size = new Vector(width, height, length);

	}

	public Building(CuboidClipboard clipBoard, WorldVector blockVector,
			Player ownerIn, String typeIn) {
		this(new Location(ownerIn.getWorld(), blockVector.getBlockX() - 1,
				blockVector.getBlockY() + 1, blockVector.getBlockZ() - 1),
				new Location(ownerIn.getWorld(), blockVector.getBlockX() - 1
						+ clipBoard.getWidth(), blockVector.getBlockY() + 1
						+ clipBoard.getHeight(), blockVector.getBlockZ() - 1
						+ clipBoard.getLength()), ownerIn, typeIn);
	}

	public Location getBottom() {
		return bottom;
	}

	public Location getTop() {
		return top;
	}

	public boolean containsBlock(Block block) {
		Location location = block.getLocation();
		return containsBlock(location);
	}

	public boolean containsBlock(Location location) {
		boolean contains = false;
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		int minX = bottom.getBlockX();
		int minY = bottom.getBlockY();
		int minZ = bottom.getBlockZ();
		int maxX = top.getBlockX();
		int maxY = top.getBlockY();
		int maxZ = top.getBlockZ();

		if (minX <= x && x <= maxX) {
			if (minY <= y && y <= maxY) {
				if (minZ < z && z <= maxZ) {
					contains = true;
				}
			}
		}
		return contains;
	}

	public int getId() {
		return id;
	}

	public Player getOwner() {
		return owner;
	}

	public Block getClosesBlockFromLocation(Location location) {
		Block block = null;
		double distance = 1000;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Location testLocation = new Location(world,
							bottom.getBlockX() + x, bottom.getBlockY() + y,
							bottom.getBlockZ() + z);
					if (location.distance(new Location(world, x, y, z)) < distance) {
						distance = location.distance(testLocation);
						block = world.getBlockAt(testLocation);
					}
				}
			}
		}
		return block;
	}

	public int getDistanceFromLocation(Location location) {
		int minX = bottom.getBlockX();
		int minY = bottom.getBlockY();
		int minZ = bottom.getBlockZ();
		int maxX = top.getBlockX();
		int maxY = top.getBlockY();
		int maxZ = top.getBlockZ();
		int lX = (int) location.getX();
		int lY = (int) location.getY();
		int lZ = (int) location.getZ();
		int dX = 0, dY = 0, dZ = 0;

		if (lX < minX) {
			dX = minX - lX;
		} else if (lX > maxX) {
			dX = lX - maxX;
		}

		if (lY < minY) {
			dY = minY - lY;
		} else if (lY > maxY) {
			dY = lY - maxY;
		}

		if (lZ < minZ) {
			dZ = minZ - lZ;
		} else if (lZ > maxZ) {
			dZ = maxZ - lZ;
		}

		return dX * dX + dY * dY + dZ * dZ;
	}

	public Vector getSize() {
		return size;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getLength() {
		return length;
	}

	public void blockSet() {
		setBlocks++;
		percentageBuilt = Math
				.round((setBlocks * 100 / totalBlocks * 100) / 100);
		buildSpeed = (int) Math.round(percentageBuilt / 10);
	}

	public double getPercentageBuilt() {
		return percentageBuilt;
	}

	public int getBuildSpeed() {
		return buildSpeed;
	}

}
