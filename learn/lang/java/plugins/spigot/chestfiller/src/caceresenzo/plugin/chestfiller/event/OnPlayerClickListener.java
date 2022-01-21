package caceresenzo.plugin.chestfiller.event;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OnPlayerClickListener implements Listener {
	
	@EventHandler
	private void onPlayerClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking()) {
			return;
		}
		
		ItemStack selectedItem = event.getItem();
		if (selectedItem == null) {
			return;
		}
		
		if ("".equals(selectedItem.getItemMeta().getDisplayName())) {
			return;
		}
		
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}
		
		BlockState blockState = block.getState();
		if (!(blockState instanceof Container)) {
			return;
		}
		
		Container container = (Container) blockState;
		Inventory inventory = container.getInventory();
		
		int size = inventory.getSize();
		int skipped = 0;
		
		for (int index = 0; index < size; index++) {
			ItemStack itemStack = inventory.getItem(index);
			
			if (itemStack != null) {
				skipped++;
				continue;
			}
			
			inventory.setItem(index, selectedItem.asOne());
			
			if (selectedItem.add(-1).getAmount() == 0) {
				break;
			}
		}
		
		if (skipped == size) {
			player.sendMessage("Container is full, skipped");
		}
		
		event.setCancelled(true);
	}
	
}