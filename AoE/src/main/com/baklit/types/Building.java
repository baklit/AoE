package main.com.baklit.types;

import main.com.baklit.events.BuildingBuiltEvent;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Building extends Object{
	
	Location Bottom;
	Location Top;
	Player owner;
	int Id = BuildingBuiltEvent.buildings.size();
	
	public Building(Location min, Location max, Player ownerIn){
		
		Bottom = min;
		Top = max;
		owner = ownerIn;
		
	}
	
	public Location getBottom(){
		return Bottom;
	}
	
	public Location getTop(){
		return Top;
	}
	
	
	public boolean containsBlock(Block block){
		boolean contains = false;
		Location location = block.getLocation();
		int X = location.getBlockX();
		int Y = location.getBlockY();
		int Z = location.getBlockZ();
		int minX = Bottom.getBlockX();
		int minY = Bottom.getBlockY();
		int minZ = Bottom.getBlockZ();
		int maxX = Top.getBlockX();
		int maxY = Top.getBlockY();
		int maxZ = Top.getBlockZ();
		
		if(minX<=X && X <= maxX){
			if(minY<= Y && Y <= maxY){
				if(minZ < Z && Z <= maxZ){
					contains = true;	
				}
			}
		}
		return contains;
	}
	
	public boolean containsBlock(Location location){
		boolean contains = false;
		int X = location.getBlockX();
		int Y = location.getBlockY();
		int Z = location.getBlockZ();
		int minX = Bottom.getBlockX();
		int minY = Bottom.getBlockY();
		int minZ = Bottom.getBlockZ();
		int maxX = Top.getBlockX();
		int maxY = Top.getBlockY();
		int maxZ = Top.getBlockZ();
		
		if(minX<=X && X <= maxX){
			if(minY<= Y && Y <= maxY){
				if(minZ < Z && Z <= maxZ){
					contains = true;	
				}
			}
		}
		return contains;
	}
	
	public int getId(){
		return Id;
	}
	
	public Player getOwner(){
		return owner;
	}

}
