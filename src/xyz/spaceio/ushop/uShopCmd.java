package xyz.spaceio.ushop;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class uShopCmd implements CommandExecutor {

	Main plugin;

	public uShopCmd(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args) {
		if (!cs.hasPermission("ushop.admin")) {
			cs.sendMessage("§cYou dont have permissions to use this command!");
			return true;
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				cs.sendMessage("§aConfig.yml reloaded!");
				plugin.reloadConfig();
				return true;
			} else if (args[0].equalsIgnoreCase("setprice")) {
				if (args.length > 1) {
					if (!(cs instanceof Player)) {
						cs.sendMessage("You have to be a player!");
						return true;
					}
					Player p = (Player) cs;
					if (p.getInventory().getItemInMainHand() != null) {
						if (p.getInventory().getItemInMainHand().getType() != null) {
							if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
								// item holding
								ItemStack inHand = p.getInventory().getItemInMainHand();
								double price = Double.parseDouble(args[1]);
								
								Optional<CustomItem> result = plugin.findCustomItem(inHand);
								if(result.isPresent()) {
									plugin.getCustomItems().remove(result.get());
									p.sendMessage("§aSuccessfully updated item:");
								}else {
									p.sendMessage("§aSuccessfully added item:");
								}
								CustomItem i = new CustomItem(p.getInventory().getItemInMainHand(), price);
								plugin.addCustomItem(i);
								plugin.saveMainConfig();
								
								p.sendMessage(plugin.getCustomItemDescription(i, 1).stream().toArray(String[]::new));
								return true;
							}
						}
					}
					cs.sendMessage("§cYou need to hold an item in your hand!");
					return true;
				}
			}
		}

		showHelp(cs);

		return true;
	}

	private void showHelp(CommandSender cs) {
		cs.sendMessage("§uShop help:");
		cs.sendMessage("/ushop §creload §r- reloads the config");
		cs.sendMessage(
				"/ushop §csetprice <price> §r- sets a custom price for an item with custom lore, displayname, durability and enchants");
	}

}