package br.com.bernardo;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NoobKit {

    public void giveKit(Player player) {
        player.getInventory().clear();

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemStack axe = new ItemStack(Material.STONE_AXE);

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 20);

        player.getInventory().addItem(sword, pickaxe, axe, soup);
    }
}
