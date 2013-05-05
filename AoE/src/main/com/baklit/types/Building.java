package main.com.baklit.types;

import main.com.baklit.AoE;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;

public class Building extends Object{
	
	Location Bottom;
	Location Top;
	Player owner;
	World world;
	Vector size;
	boolean contains;
	int buildSpeed;
	int totalBlocks;
	int setBlocks;
	int percentageBuilt;
	int width;
	int height;
	int length;
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
		world = Bottom.getWorld();
		
		width = Top.getBlockX() - Bottom.getBlockX();
		height = Top.getBlockY() - Bottom.getBlockY();
		length = Top.getBlockZ() - Bottom.getBlockZ();
		
		totalBlocks = width*height*length;
		
		size = new Vector(width, height, length);
	}
	
	public Building(CuboidClipboard clipBoard, WorldVector blockVector, Player ownerIn){
		
		Location minLoc = new Location(ownerIn.getWorld(), blockVector.getBlockX()-1,blockVector.getBlockY()+1,blockVector.getBlockZ()-1);
		Location maxLoc = new Location(ownerIn.getWorld(), blockVector.getBlockX()-1+clipBoard.getWidth(),blockVector.getBlockY()+1+clipBoard.getHeight(),blockVector.getBlockZ()-1 + clipBoard.getLength());
		
		Bottom = minLoc;
		Top = maxLoc;
		owner = ownerIn;
		world = Bottom.getWorld();
		
		width = Top.getBlockX() - Bottom.getBlockX();
		height = Top.getBlockY() - Bottom.getBlockY();
		length = Top.getBlockZ() - Bottom.getBlockZ();
		
		ownerIn.sendMessage("width " + width + " height " + height + " length " + length);
		
		totalBlocks = width*height*length;
		
		size = new Vector(width, height, length);
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
		Block block = null;
		double distance = 1000;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				for(int z = 0; z < length; z++ ){
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
		double distance = 1000;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				for(int z = 0; z < length; z++ ){
					Location testLocation = new Location(world,Bottom.getBlockX()+x,Bottom.getBlockY()+y,Bottom.getBlockZ()+z);
					if(location.distance(testLocation) < distance){
						distance = location.distance(testLocation);
					}
				}
			}
		}
		return (int) Math.round(distance);
	}
	
	public Vector getSize(){
		return size;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getLength(){
		return length;
	}
	
	public void blockSet(){
		setBlocks++;
		percentageBuilt = Math.round((setBlocks*100/totalBlocks*100)/100);
		System.out.println(setBlocks + "  /  " + totalBlocks);
		buildSpeed = percentageBuilt/10;
	}
	
	public int getPercentageBuilt(){
		return percentageBuilt;
	}
	
	public int getBuildSpeed(){
		return buildSpeed;
	}

}
