package main.com.baklit.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingBuiltEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();

	
	public BuildingBuiltEvent(Location minIn, Location maxIn, Player player){
	}
	
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
