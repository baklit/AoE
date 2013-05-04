package main.com.baklit.events;

import main.com.baklit.AoE;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;

public class BuildingPlacedEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	public BuildingPlacedEvent(final Player playerIn, final CuboidClipboard clipBoardIn, final WorldVector blockVectorIn){
		
		final World worldIn = playerIn.getWorld();
		
		Bukkit.getServer().getScheduler().runTaskAsynchronously(AoE.instance, new BukkitRunnable(){
			
			public void run() {
				
				Player player = playerIn;
				World world = worldIn;
				CuboidClipboard clipBoard = clipBoardIn;
				WorldVector blockVector = blockVectorIn;
				
				for (int i=0;i<clipBoard.getHeight();i++){
					for (int o=0;o<clipBoard.getWidth();o++){
						for(int p=0;p<clipBoard.getLength();p++){
							try {
								Thread.currentThread();
								Thread.sleep(250);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Location blockLocation = new Location(world, blockVector.getBlockX()+o,blockVector.getBlockY()+1+i,blockVector.getBlockZ()+p);
							BaseBlock testBlock = clipBoard.getPoint(new Vector(o,i,p));
							world.getBlockAt(blockLocation).setTypeIdAndData(testBlock.getId(),(byte) testBlock.getData(), false);
						}
					}
				}
				Bukkit.getScheduler().runTask(AoE.instance, new BukkitRunnable(){

					@Override
					public void run() {
						BuildingBuiltEvent builtEvent = new BuildingBuiltEvent();
						Bukkit.getPluginManager().callEvent(builtEvent);
						
					}
					
				});


			}
		});

	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
