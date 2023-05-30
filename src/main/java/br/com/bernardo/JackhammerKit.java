package br.com.bernardo;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JackhammerKit {

    public void giveKit(Player player) {
        player.getInventory().clear();

        ItemStack jackhammerAxe = new ItemStack(Material.STONE_AXE);
        ItemMeta axeMeta = jackhammerAxe.getItemMeta();
        axeMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Jackhammer");
        jackhammerAxe.setItemMeta(axeMeta);

        player.getInventory().addItem(jackhammerAxe);
    }
}
