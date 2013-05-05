package main.com.baklit.util;

import main.com.baklit.AoE;
import main.com.baklit.events.BuildingBuiltEvent;
import main.com.baklit.types.Building;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;

public class BuildBuildingHelper extends BukkitRunnable{
	
	int i = 0;
	int o = 0;
	int p = 0;
	
	Player player;
	CuboidClipboard clipBoard;
	WorldVector blockVector;
	Building building;
	int buildSpeed;

	public void buildBuilding(final Player playerIn, final CuboidClipboard clipBoardIn, final WorldVector blockVectorIn, final Building buildingIn){
		
		player = playerIn;
		clipBoard = clipBoardIn;
		blockVector = blockVectorIn;
		building = buildingIn;
		buildSpeed = building.getBuildSpeed();
		
		this.runTaskLater(AoE.instance, building.getBuildSpeed());
		
	}
	

	public void run() {
		
		World world = player.getWorld();
		Location blockLocation = new Location(world, blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
		BaseBlock testBlock = clipBoard.getPoint(new Vector(o,i,p));
		world.getBlockAt(blockLocation).setTypeIdAndData(testBlock.getId(),(byte) testBlock.getData(), false);
		building.blockSet();
		
		p++;
		
		if(p == clipBoard.getLength()){
			p = 0;
			o++;
		}
		if(o == clipBoard.getWidth()){
			o = 0;
			i++;
		}
		if(i == clipBoard.getHeight()){
			
			
			Location minLoc = new Location(world, blockVector.getBlockX()-1,blockVector.getBlockY()+1,blockVector.getBlockZ()-1);
			Location maxLoc = new Location(world, blockVector.getBlockX()-1+clipBoard.getWidth(),blockVector.getBlockY()+clipBoard.getHeight(),blockVector.getBlockZ()-1 + clipBoard.getLength());
			BuildingBuiltEvent builtEvent = new BuildingBuiltEvent(minLoc, maxLoc, player);
			Bukkit.getPluginManager().callEvent(builtEvent);
					
				
			this.cancel();
		}
		this.runTaskLater(AoE.instance, building.getBuildSpeed());

	}
	

}
