package caceresenzo.plugin.chestfiller;

import org.bukkit.plugin.java.JavaPlugin;

import caceresenzo.plugin.chestfiller.event.OnPlayerClickListener;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new OnPlayerClickListener(), this);
	}
	
	@Override
	public void onDisable() {
	}
	
}