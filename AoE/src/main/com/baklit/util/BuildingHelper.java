package main.com.baklit.util;

import main.com.baklit.AoE;
import main.com.baklit.types.Building;

import org.bukkit.block.Block;

public class BuildingHelper {
	
	static Building building;
	
	public static Building getBuildingFromBlock(Block block){
		building = null;
		for(Building buildings : AoE.buildings.keySet()){
			
			if(buildings.containsBlock(block) == true){
				building = buildings;
				break;
			}
		
		}
		return building;
	}
}
