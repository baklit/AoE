package main.com.baklit.events;

import java.util.HashMap;

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
		final HashMap<Integer, Integer> runi = new HashMap<Integer, Integer>();
		final HashMap<Integer, Integer> runo = new HashMap<Integer, Integer>();
		final HashMap<Integer, Integer> runp = new HashMap<Integer, Integer>();
		
		new BukkitRunnable(){
			
			
			public void run() {
				
				if(runi.get(this.getTaskId()) == null){
					System.out.println("setting i");
					runi.put(this.getTaskId(), 0);
				}
				
				if(runo.get(this.getTaskId()) == null){
					runo.put(this.getTaskId(), 0);
				}
				
				if(runp.get(this.getTaskId()) == null){
					runp.put(this.getTaskId(), 0);
				}
				
				int i = runi.get(this.getTaskId());
				int o = runo.get(this.getTaskId());
				int p = runp.get(this.getTaskId());
				
				Player player = playerIn;
				World world = worldIn;
				CuboidClipboard clipBoard = clipBoardIn;
				WorldVector blockVector = blockVectorIn;
				
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

						@Override
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
				
				runi.put(this.getTaskId(), i);
				runo.put(this.getTaskId(), o);
				runp.put(this.getTaskId(), p);

			}
		}.runTaskTimer(AoE.instance, 0, 5);

	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
