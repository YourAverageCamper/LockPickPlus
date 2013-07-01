
package me.zeus.LockPickPlus;


import java.io.File;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;



public class LockPickPlus extends JavaPlugin implements Listener
{
	
	
	String badPermsMSG;
	String successMSG;
	String failMSG;
	int chance;
	
	
	
	public void onEnable()
	{
		File config = new File(getDataFolder() + "/config.yml");
		if (!config.exists())
		{
			getLogger().info("Config not found, creating default one...");
			saveDefaultConfig();
		}
		
		badPermsMSG = getConfig().getString("permission-message").replace("&", "§");
		successMSG = getConfig().getString("success-message").replace("&", "§");
		chance = getConfig().getInt("lockpick-chance");
		failMSG = getConfig().getString("fail-message").replace("&", "§");
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack is = p.getItemInHand();
		
		if (is == null)
			return;
		
		if (is != null && is.getType().equals(Material.IRON_INGOT))
		{
			if (e.getClickedBlock() == null)
				return;
			if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST))
			{
				Block clicked = e.getClickedBlock();
				if (clicked.getState() instanceof Chest)
				{
					int r = new Random().nextInt(100) + 1;
					if (chance >= r)
					{
						p.sendMessage(successMSG);
						Chest chest = (Chest) clicked.getState();
						p.openInventory(chest.getInventory());
						p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 1) });
					}
					else
					{
						p.sendMessage(failMSG);
					}
				}
				else if (clicked.getState() instanceof DoubleChest)
				{
					int r = new Random().nextInt(100) + 1;
					if (chance >= r)
					{
						p.sendMessage(successMSG);
						DoubleChest chest = (DoubleChest) clicked.getState();
						p.openInventory(chest.getInventory());
					}
					else
					{
						p.sendMessage(failMSG);
					}
				}
			}
		}
	}
	
}
