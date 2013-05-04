package main.com.baklit.util;

import main.com.baklit.AoE;
import main.com.baklit.events.BuildingBuiltEvent;

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

	public void buildBuilding(final Player playerIn, final CuboidClipboard clipBoardIn, final WorldVector blockVectorIn){
		
		player = playerIn;
		clipBoard = clipBoardIn;
		blockVector = blockVectorIn;
		
		this.runTaskTimer(AoE.instance, 0, 1);
		
	}
	

	public void run() {
		
		World world = player.getWorld();
		Location blockLocation = new Location(world, blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
		BaseBlock testBlock = clipBoard.getPoint(new Vector(o,i,p));
		world.getBlockAt(blockLocation).setTypeIdAndData(testBlock.getId(),(byte) testBlock.getData(), false);
		
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
					
			BuildingBuiltEvent builtEvent = new BuildingBuiltEvent(new Location(world,0,0,0), new Location(world,o,i,p), player);
			Bukkit.getPluginManager().callEvent(builtEvent);
					
				
			this.cancel();
		}

	}

}
