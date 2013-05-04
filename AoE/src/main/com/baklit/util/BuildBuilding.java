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

public class BuildBuilding extends BukkitRunnable{
	
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
		
		this.runTaskTimer(AoE.instance, 0, 5);
		
	}
	

	public void run() {
		
		World world = player.getWorld();
		
		if(p == clipBoard.getLength()){
			p = 0;
			o++;
		}
		if(o == clipBoard.getWidth()){
			o = 0;
			i++;
		}
		if(i == clipBoard.getHeight()){
			i = 0;
			
			new BukkitRunnable(){

				public void run() {
					BuildingBuiltEvent builtEvent = new BuildingBuiltEvent();
					Bukkit.getPluginManager().callEvent(builtEvent);
					
				}
				
			}.runTask(AoE.instance);
			this.cancel();
		}
		
		Location blockLocation = new Location(world, blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
		BaseBlock testBlock = clipBoard.getPoint(new Vector(o,i,p));
		world.getBlockAt(blockLocation).setTypeIdAndData(testBlock.getId(),(byte) testBlock.getData(), false);
		System.out.println("place block at " + o + " " + i+ " " + p);
		
		p++;

	}

}
