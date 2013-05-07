package main.com.baklit.types;

import main.com.baklit.AoE;
import main.com.baklit.ai.traits.BasicVillagerTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NpcBase {

	static BasicVillagerTrait basicVillagerTrait;

	public static void Npc(Location location, String type, Player ownerIn) {

		NPCRegistry npcregistry = CitizensAPI.getNPCRegistry();
		NPC npc = npcregistry.createNPC(EntityType.PLAYER,
				type + AoE.npcs.size());
		npc.spawn(location);
		npc.addTrait(basicVillagerTrait);
		AoE.npcs.put(npc, ownerIn);

	}

}
