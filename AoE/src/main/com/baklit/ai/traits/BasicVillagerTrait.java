package main.com.baklit.ai.traits;

import main.com.baklit.AoE;
import main.com.baklit.communication.Communications;
import main.com.baklit.events.BuildingPlacedEvent;
import main.com.baklit.util.NamedString;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;

public class BasicVillagerTrait extends Trait {

	AoE plugin;

	protected BasicVillagerTrait(String name) {
		super(name);
	}

	public BasicVillagerTrait() {
		super("BasicVillagerTrait");
		plugin = (AoE) Bukkit.getServer().getPluginManager().getPlugin("AoE");
	}

	public LocalSession getSessionFromPlayer(Player player) {
		LocalSession localsession;
		localsession = WorldEdit.getInstance().getSession(
				player.getDisplayName());

		return localsession;
	}

	@EventHandler
	void onBuildingPlace(BuildingPlacedEvent event) {
	}

	@Override
	public void run() {

	}

	@Override
	public void onAttach() {
		Communications.sendMessage(Communications.VILLAGER_ATTACHED,
				new NamedString("name", getName()));
	}

	@Override
	public void onDespawn() {

	}

	@Override
	public void onSpawn() {

	}

	@Override
	public void onRemove() {

	}

}
