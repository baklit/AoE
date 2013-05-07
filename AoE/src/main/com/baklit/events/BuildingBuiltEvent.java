package main.com.baklit.events;

import main.com.baklit.types.NpcBase;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildingBuiltEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public BuildingBuiltEvent(Location minIn, Location maxIn, Player player) {

		NpcBase.Npc(minIn, "BasicVillager", player);

	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
