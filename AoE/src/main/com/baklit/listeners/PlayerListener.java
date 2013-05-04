package main.com.baklit.listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import main.com.baklit.AoE;
import main.com.baklit.events.BuildingBuiltEvent;
import main.com.baklit.events.BuildingPlacedEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class PlayerListener implements Listener{
	
	public static CuboidClipboard house;
	public static Player testPlayer;
	public static WorldVector blockVector;
	public static HashMap<Player, CuboidClipboard> playersClipboard = new HashMap<Player, CuboidClipboard>();
	Boolean updateSent = false;
	Location blockLocation;
	Block blockLookedAt;
	HashMap<Player, Long> OverFlowStop = new HashMap<Player, Long>();
	HashMap<Location, Player> blocksGhosted = new HashMap<Location, Player>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) throws IOException, DataException{
		if(Bukkit.getServer().getPlayer(event.getPlayer().getDisplayName()) != null){
			if(event.getPlayer().getItemInHand().getType() == Material.STICK){
				if(OverFlowStop.get(event.getPlayer()) == null){
					OverFlowStop.put(event.getPlayer(), System.currentTimeMillis());
				}
				if(System.currentTimeMillis() - OverFlowStop.get(event.getPlayer()) <= 500){
				}
				else{
					OverFlowStop.put(event.getPlayer(), System.currentTimeMillis());
				if(updateSent == true){
					for(Location location : blocksGhosted.keySet()){
						Player player = blocksGhosted.get(location);
						if(player.getDisplayName().equals(event.getPlayer().getDisplayName())){
							Block originalBlock = event.getPlayer().getWorld().getBlockAt(location);
							event.getPlayer().sendBlockChange(location, originalBlock.getType(), originalBlock.getData());
						}
					}
				}
				CuboidClipboard house = playersClipboard.get(event.getPlayer());
				if(house == null){
					event.getPlayer().sendMessage(ChatColor.RED + "[AoE] Please select a building first");
				}
				else{
				LocalPlayer localPlayer = new WorldEditPlugin().wrapPlayer(event.getPlayer());
				WorldVector blockVector = localPlayer.getSolidBlockTrace(200);
				if (blockVector != null){ 
					boolean valid = true;
					for(int length=0;length < house.getLength();length++){
						for(int width=0;width < house.getWidth();width++){
							Location testLocation = new Location(event.getPlayer().getWorld(), blockVector.getBlockX()+length,blockVector.getBlockY(),blockVector.getBlockZ()+width);
							if(!event.getPlayer().getWorld().getBlockAt(testLocation).getType().equals(Material.STONE)){
								valid = false;
							}
						}
						
					}
					if(valid != false){
				for (int i=0;i<house.getHeight();i++){
					for (int o=0;o<house.getWidth();o++){
						for(int p=0;p<house.getLength();p++){
							blockLocation = new Location(event.getPlayer().getWorld(), blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
							event.getPlayer().sendBlockChange(blockLocation, Material.WOOL, (byte) 5);
							blocksGhosted.put(blockLocation, event.getPlayer());
						}
					}
				}
					}
					else{
						for (int i=0;i<house.getHeight();i++){
							for (int o=0;o<house.getWidth();o++){
								for(int p=0;p<house.getLength();p++){
									blockLocation = new Location(event.getPlayer().getWorld(), blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
									event.getPlayer().sendBlockChange(blockLocation, Material.WOOL, (byte) 14);
									blocksGhosted.put(blockLocation, event.getPlayer());
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
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.STICK){
		Bukkit.getServer().broadcastMessage("" + event.getAction().name());
		
		try {
			playersClipboard.put(event.getPlayer(), SchematicFormat.MCEDIT.load(new File("plugins/AoE/Schematics/BasicHouse.schematic")));
		} catch (IOException | DataException e) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "[AoE] Sorry but an error occured trying to load that building, please try that action again.");
		}
	
		
		LocalPlayer localPlayer = new WorldEditPlugin().wrapPlayer(event.getPlayer());
		blockVector = localPlayer.getSolidBlockTrace(200);
		CuboidClipboard clipBoard = playersClipboard.get(event.getPlayer());
		
		Event BuildingPlacedEvent = new BuildingPlacedEvent(event.getPlayer(), clipBoard, blockVector);
		Bukkit.getPluginManager().callEvent(BuildingPlacedEvent);
		
		}

	}
}
