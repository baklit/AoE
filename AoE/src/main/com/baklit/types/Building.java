package main.com.baklit.types;

import main.com.baklit.AoE;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.WorldVector;

public class Building extends Object{
	
	Location Bottom;
	Location Top;
	Player owner;
	boolean contains;
	int Id = AoE.buildings.size();
	int X;
	int Y;
	int Z;
	int minX;
	int minY;
	int minZ;
	int maxX;
	int maxY;
	int maxZ;
	
	public Building(Location min, Location max, Player ownerIn){
		
		Bottom = min;
		Top = max;
		owner = ownerIn;
		
	}
	
	public Building(CuboidClipboard clipBoard, WorldVector blockVector, Player ownerIn){
		Location minLoc = new Location(ownerIn.getWorld(), blockVector.getBlockX()-1,blockVector.getBlockY()+1,blockVector.getBlockZ()-1);
		Location maxLoc = new Location(ownerIn.getWorld(), blockVector.getBlockX()-1+clipBoard.getWidth(),blockVector.getBlockY()+clipBoard.getHeight(),blockVector.getBlockZ()-1 + clipBoard.getLength());
		Bottom = minLoc;
		Top = maxLoc;
		owner = ownerIn;
		System.out.println(blockVector.getBlockX()-1);
		System.out.println(blockVector.getBlockY()+1);
		System.out.println(blockVector.getBlockZ()-1);
		System.out.println(blockVector.getBlockX()-1+clipBoard.getWidth());
		System.out.println(blockVector.getBlockY()+clipBoard.getHeight());
		System.out.println(blockVector.getBlockZ()-1 + clipBoard.getLength());
		}
	
	public Location getBottom(){
		return Bottom;
	}
	
	public Location getTop(){
		return Top;
	}
	
	
	public boolean containsBlock(Block block){
		contains = false;
		Location location = block.getLocation();
		X = location.getBlockX();
		Y = location.getBlockY();
		Z = location.getBlockZ();
		minX = Bottom.getBlockX();
		minY = Bottom.getBlockY();
		minZ = Bottom.getBlockZ();
		maxX = Top.getBlockX();
		maxY = Top.getBlockY();
		maxZ = Top.getBlockZ();
		
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
		contains = false;
		X = location.getBlockX();
		Y = location.getBlockY();
		Z = location.getBlockZ();
		minX = Bottom.getBlockX();
		minY = Bottom.getBlockY();
		minZ = Bottom.getBlockZ();
		maxX = Top.getBlockX();
		maxY = Top.getBlockY();
		maxZ = Top.getBlockZ();
		
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
	
	public Block getClosesBlockFromLocation(Location location){
		World world = Bottom.getWorld();
		Block block = null;
		double distance = 0;
		int sizeX = Bottom.getBlockX() - Top.getBlockX();
		int sizeY = Bottom.getBlockZ() - Top.getBlockZ();
		int sizeZ = Bottom.getBlockY() - Top.getBlockY();
		
		for(int x = 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				for(int z = 0; z < sizeZ; z++ ){
					Location testLocation = new Location(world,Bottom.getBlockX()+x,Bottom.getBlockY()+y,Bottom.getBlockZ()+z);
					if(location.distance(new Location(world, x,y,z)) < distance){
						distance = location.distance(testLocation);
						block = world.getBlockAt(testLocation);
					}
				}
			}
		}
		return block;
	}
	
	public int getDistanceFromLocation(Location location){
		World world = Bottom.getWorld();
		double distance = 1000;
		int sizeX = Top.getBlockX() - Bottom.getBlockX();
		int sizeY = Top.getBlockZ() - Bottom.getBlockZ();
		int sizeZ = Top.getBlockY() - Bottom.getBlockY();
		
		for(int x = 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				for(int z = 0; z < sizeZ; z++ ){
					Location testLocation = new Location(world,Bottom.getBlockX()+x,Bottom.getBlockY()+y,Bottom.getBlockZ()+z);
					System.out.println(location.distance(testLocation));
					if(location.distance(testLocation) < distance){
						distance = location.distance(testLocation);
					}
				}
			}
		}
		return (int) Math.round(distance);
	}

}
