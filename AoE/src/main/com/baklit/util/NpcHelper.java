package main.com.baklit.util;

import java.util.List;

import main.com.baklit.AoE;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class NpcHelper {

	public static final float SIGHT_INACCURRACY_SQUARED = 9;

	public static void selectNpc(Player player) {
		TESTING2(player);
		// NPC lookingAt = getLookingAt(player);
		// player.sendMessage(lookingAt == null ? "nothing" :
		// lookingAt.getFullName()); // testing
	}

	public static NPC getLookingAt(Player player) {
		World world = player.getWorld();

		// for(NPC ent : AoE.npcs.keySet()) {
		// player.sendMessage("Checking " + ent.getFullName() + "(" +
		// ent.getClass().getCanonicalName() + ")");
		// if(isLookingAt(player, ent))
		// return (NPC) ent;
		// }
		return null;
	}

	public static boolean isLookingAt(Player player, NPC other) {
		if (!player.hasLineOfSight(other.getBukkitEntity()))
			return false;
		float pitch = player.getEyeLocation().getPitch();
		float yaw = player.getEyeLocation().getYaw();
		double xzLen = Math.cos(pitch);
		double changeX = xzLen * Math.cos(yaw);
		double changeY = Math.sin(pitch);
		double changeZ = xzLen * Math.sin(-yaw);

		Location startLocation = player.getLocation();
		double startX = startLocation.getX();
		double startY = startLocation.getY() + 1;
		double startZ = startLocation.getZ();

		Location checkPoint = other.getBukkitEntity().getLocation();
		double checkX = checkPoint.getX() + 0.5;
		double checkY = checkPoint.getY() + 0.25;
		double checkZ = checkPoint.getZ() + 0.25;

		double xDiff = checkX - startX;
		double yDiff = checkY - startY;
		double zDiff = checkZ - startZ;

		double magicNumX = xDiff - changeX;
		double magicNumY = yDiff - changeY;
		double magicNumZ = zDiff - changeZ;

		double diffFromLOS = (magicNumX * magicNumX) + (magicNumY * magicNumY)
				+ (magicNumZ * magicNumZ);
		System.out.println(diffFromLOS);
		player.sendMessage("start: " + startX + ", " + startY + ", " + startZ);
		player.sendMessage("change: " + changeX + ", " + changeY + ", "
				+ changeZ);
		player.sendMessage("check: " + checkX + ", " + checkY + ", " + checkZ);
		player.sendMessage("diff: " + xDiff + ", " + yDiff + ", " + zDiff);
		player.sendMessage("magicNum: " + magicNumX + ", " + magicNumY + ", "
				+ magicNumZ);
		player.sendMessage(ChatColor.GREEN + " " + Math.sqrt(diffFromLOS));
		return diffFromLOS < SIGHT_INACCURRACY_SQUARED;

	}

	public static void TESTINGgetLookingAt(Player player) {

		// Player look up = pitch = -90
		// Player look down = pitch = 90
		// Player look around = yaw = 0 AT SOUTH
		// Yaw 0 = Z increase
		// Yaw 90 = X Decrease
		// Yaw 180 = Z Decrease
		// Yaw 270 = X increase

		// Yaw/360 = rotation percentage?

		// if 0.25<= rp <0.75 then Z decrease else Z increase

		// if(1-rp < 0.25) Z change amount = (1-rp)*4
		// if(

		// Z change amount ==

		Location eyeLocation = player.getEyeLocation();
		Location playersLocation = player.getLocation();
		Location changedLocation;
		World world = player.getWorld();
		float playersPitch = eyeLocation.getPitch();
		float playersYaw = eyeLocation.getYaw();
		double playersX = playersLocation.getX();
		double playersY = playersLocation.getY();
		double playersZ = playersLocation.getZ();
		double playersXZRotation = playersYaw / 360;
		double playersYRotation = playersPitch / 90;
		double changeMultiplyer = 20;
		double zChange = 999;
		double xChange = 999;
		double yAxisModifier = 1;
		double changedX;
		double changedY;
		double changedZ;
		// double testyawX;
		// double testyawZ;
		// double testyaw;
		// double testyawY;

		if (playersYaw < 0) {
			playersYaw = -playersYaw;
		}

		// testyaw = Math.cos(playersPitch);
		// testyawX = testyaw * Math.cos(playersYaw);
		// testyawY = Math.sin(playersPitch);
		// testyawZ = testyaw * Math.sin(-playersYaw);
		//
		//
		// player.sendMessage("testyaw : " + testyaw);
		// player.sendMessage("testyawX : " + (1 - testyawX));
		// player.sendMessage("testyawY : " + testyawY);
		// player.sendMessage("testyawZ : " + (testyawZ));

		for (changeMultiplyer = 5; changeMultiplyer < 200; changeMultiplyer++) {
			if (1 - playersXZRotation <= 0.25) {
				zChange = 1 - ((1 - playersXZRotation) * 4);
				xChange = (1 - playersXZRotation) * 4;
			}
			if (0.25 < 1 - playersXZRotation && 1 - playersXZRotation <= 5) {
				zChange = 1 - ((1 - playersXZRotation) * 4);
				xChange = 1 - (((1 - playersXZRotation) * 4) - 1);
			}
			if (0.5 < 1 - playersXZRotation && 1 - playersXZRotation <= 0.75) {
				zChange = 1 - (playersXZRotation * 4);
				xChange = -(1 - ((playersXZRotation * 4) - 1));
			}
			if (0.75 < 1 - playersXZRotation && 1 - playersXZRotation <= 1) {
				zChange = 1 - (playersXZRotation * 4);
				xChange = -(playersXZRotation * 4);
			}
			if (-1 <= playersYRotation && playersYRotation < 0) {
				yAxisModifier = playersYRotation + 1;
			}
			if (0 <= playersYRotation && playersYRotation <= 1) {
				yAxisModifier = -(playersYRotation - 1);
			}

			player.sendMessage("xChange : " + xChange);
			player.sendMessage("zChange : " + zChange);

			changedX = playersX + (xChange * changeMultiplyer * yAxisModifier);
			changedY = playersY - (playersYRotation * changeMultiplyer);
			changedZ = playersZ + (zChange * changeMultiplyer * yAxisModifier);

			changedLocation = new Location(world, changedX, changedY, changedZ);
			player.sendBlockChange(changedLocation, Material.WOOD, (byte) 0);
		}
	}

	public static void TESTING2(Player player) {

		Location blockLocation;
		Location NpcLocation;

		List<Block> blocks = player.getLineOfSight(null, 200);
		for (Block block : blocks) {
			blockLocation = block.getLocation();
			for (NPC npc : AoE.npcs.keySet()) {
				NpcLocation = npc.getBukkitEntity().getLocation();
				if (blockLocation.distanceSquared(NpcLocation) <= 4) {
					player.sendMessage("Looking at npc : " + npc.getName());
					return;
				}
			}
		}
		player.sendMessage("Npc not found");

	}

}
