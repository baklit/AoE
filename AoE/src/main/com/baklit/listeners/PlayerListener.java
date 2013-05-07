package main.com.baklit.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.baklit.AoE;
import main.com.baklit.communication.Communications;
import main.com.baklit.events.BuildingPlacedEvent;
import main.com.baklit.types.Building;
import main.com.baklit.util.BuildingHelper;
import main.com.baklit.util.NamedString;
import main.com.baklit.util.NpcHelper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class PlayerListener implements Listener {

	public static CuboidClipboard house;
	public static Player testPlayer;
	public static WorldVector blockVector;
	public static HashMap<Player, CuboidClipboard> playersClipboard = new HashMap<Player, CuboidClipboard>();
	Boolean updateSent = false;
	Location blockLocation;
	Block blockLookedAt;
	HashMap<Player, Long> OverFlowStop = new HashMap<Player, Long>();
	HashMap<Location, Player> blocksGhosted = new HashMap<Location, Player>();

	private List<Location> toRemoveReuse;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) throws IOException,
			DataException {
		if (toRemoveReuse == null) {
			toRemoveReuse = new ArrayList<>();
		} else {
			toRemoveReuse.clear();
		}
		if (Bukkit.getServer().getPlayer(event.getPlayer().getDisplayName()) != null) {
			if (event.getPlayer().getItemInHand().getType() == Material.STICK) {
				if (OverFlowStop.get(event.getPlayer()) == null) {
					OverFlowStop.put(event.getPlayer(),
							System.currentTimeMillis());
				}
				if (System.currentTimeMillis()
						- OverFlowStop.get(event.getPlayer()) <= 50) {
				} else {
					OverFlowStop.put(event.getPlayer(),
							System.currentTimeMillis());
					if (updateSent == true) {

						for (Location location : blocksGhosted.keySet()) {
							Player player = blocksGhosted.get(location);
							if (player.getDisplayName().equals(
									event.getPlayer().getDisplayName())) {
								Block originalBlock = event.getPlayer()
										.getWorld().getBlockAt(location);
								event.getPlayer().sendBlockChange(location,
										originalBlock.getType(),
										originalBlock.getData());
								toRemoveReuse.add(location);
							}
						}
						for (Location l : toRemoveReuse) {
							blocksGhosted.remove(l);
						}
					}
					CuboidClipboard house = playersClipboard.get(event
							.getPlayer());
					if (house == null) {
						Communications.sendMessage(
								Communications.SELECT_A_BUILDING_FIRST,
								event.getPlayer());
					} else {
						LocalPlayer localPlayer = new WorldEditPlugin()
								.wrapPlayer(event.getPlayer());
						WorldVector blockVector = localPlayer
								.getSolidBlockTrace(25);
						if (blockVector != null) {
							boolean valid = true;
							for (int length = 0; length < house.getLength(); length++) {
								for (int width = 0; width < house.getWidth() - 1; width++) {
									Location testLocation = new Location(event
											.getPlayer().getWorld(),
											blockVector.getBlockX() + length,
											blockVector.getBlockY(),
											blockVector.getBlockZ() + width);
									if (!event.getPlayer().getWorld()
											.getBlockAt(testLocation).getType()
											.isSolid()) {
										valid = false;
									}
								}

							}
							for (int length = 0; length < house.getLength(); length++) {
								for (int width = 0; width < house.getWidth() - 1; width++) {
									for (int height = 1; height < house
											.getHeight(); height++) {
										Location testLocation = new Location(
												event.getPlayer().getWorld(),
												blockVector.getBlockX()
														+ length,
												blockVector.getBlockY()
														+ height,
												blockVector.getBlockZ() + width);
										if (event.getPlayer().getWorld()
												.getBlockAt(testLocation)
												.getType().isSolid()) {
											valid = false;
										}
									}
								}

							}
							if (valid != false) {
								for (int i = 0; i < house.getHeight(); i++) {
									for (int o = 0; o < house.getWidth(); o++) {
										for (int p = 0; p < house.getLength(); p++) {
											blockLocation = new Location(
													event.getPlayer()
															.getWorld(),
													blockVector.getBlockX() + o,
													blockVector.getBlockY() + 1
															+ i, blockVector
															.getBlockZ() + p);
											event.getPlayer().sendBlockChange(
													blockLocation,
													Material.WOOL, (byte) 5);
											blocksGhosted.put(blockLocation,
													event.getPlayer());
										}
									}
								}
							} else {
								for (int i = 0; i < house.getHeight(); i++) {
									for (int o = 0; o < house.getWidth(); o++) {
										for (int p = 0; p < house.getLength(); p++) {
											blockLocation = new Location(
													event.getPlayer()
															.getWorld(),
													blockVector.getBlockX() + o,
													blockVector.getBlockY() + 1
															+ i, blockVector
															.getBlockZ() + p);
											event.getPlayer().sendBlockChange(
													blockLocation,
													Material.WOOL, (byte) 14);
											blocksGhosted.put(blockLocation,
													event.getPlayer());
										}
									}
								}
							}
							updateSent = true;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType() == Material.STICK) {
			Bukkit.getServer().broadcastMessage("" + event.getAction().name());

			try {
				playersClipboard
						.put(event.getPlayer(),
								SchematicFormat.MCEDIT
										.load(new File(
												"plugins/AoE/Schematics/BasicHouse.schematic")));
			} catch (IOException | DataException e) {
				Communications.sendMessage(
						Communications.PLAYER_FAIL_TO_LOAD_SCHEMATIC,
						event.getPlayer());
			}

			LocalPlayer localPlayer = new WorldEditPlugin().wrapPlayer(event
					.getPlayer());
			blockVector = localPlayer.getSolidBlockTrace(25);
			CuboidClipboard clipBoard = playersClipboard.get(event.getPlayer());

			Event BuildingPlacedEvent = new BuildingPlacedEvent(
					event.getPlayer(), clipBoard, blockVector,
					AoE.playersBuildingType.get(event.getPlayer()));
			Bukkit.getPluginManager().callEvent(BuildingPlacedEvent);

		}
		if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD) {
			LocalPlayer localPlayer = new WorldEditPlugin().wrapPlayer(event
					.getPlayer());
			blockVector = localPlayer.getSolidBlockTrace(25);
			Block block = event
					.getPlayer()
					.getWorld()
					.getBlockAt(blockVector.getBlockX(),
							blockVector.getBlockY(), blockVector.getBlockZ());
			Building building = BuildingHelper.getBuildingFromBlock(block);
			if (building == null) {
				Communications.sendMessage(Communications.NOT_A_BUILDING,
						event.getPlayer());
			} else {
				BuildingHelper.selectBuilding(building, event.getPlayer());
				Communications.sendMessage(Communications.BUILDING_ID_AND_NAME,
						event.getPlayer(),
						new NamedString("id", building.getId()),
						new NamedString("owner", building.getOwner()
								.getDisplayName()));
				Communications.sendMessage(
						Communications.BUILDING_DISTANCE_AWAY,
						event.getPlayer(),
						new NamedString("distance", building
								.getDistanceFromLocation(event.getPlayer()
										.getLocation())));
			}
		}
		if (event.getPlayer().getItemInHand().getType() == Material.ARROW) {
			NpcHelper.selectNpc(event.getPlayer());

		}

	}
}
