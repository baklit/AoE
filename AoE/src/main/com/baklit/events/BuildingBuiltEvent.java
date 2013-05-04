package main.com.baklit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingBuiltEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	
	public BuildingBuiltEvent(){
		
	}
	
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
