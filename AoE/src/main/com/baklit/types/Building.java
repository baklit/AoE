package main.com.baklit.types;

import org.bukkit.Location;

public class Building extends Object{
	
	Location Bottom;
	Location Top;
	Location Door; //bottom of the door
	
	public Building(Location min, Location max, Location doorIn){
		
		Bottom = min;
		Top = max;
		Door = doorIn;
		
	}
	
	public Location getBottom(){
		return Bottom;
	}
	
	public Location getTop(){
		return Top;
	}
	
	public Location getDoor(){
		return Door;
	}

}
