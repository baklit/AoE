package main.com.baklit.events;

import java.util.HashMap;

import main.com.baklit.types.Building;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingBuiltEvent extends Event{
	
	public static HashMap<Building, Player> buildings = new HashMap<Building, Player>();
	
	private static final HandlerList handlers = new HandlerList();

	
	public BuildingBuiltEvent(Location minIn, Location maxIn, Player player){
		Building building = new Building(minIn, maxIn, player);
		buildings.put(building, player);
		Bukkit.getServer().broadcastMessage("Building : " + building.getId() + " was built and is owned by " + building.getOwner().getDisplayName());
	}
	
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
