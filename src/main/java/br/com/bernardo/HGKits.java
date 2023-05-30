package br.com.bernardo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class HGKits extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Iniciando o plugin HG Kits");

        getCommand("kit").setExecutor(this);
        getCommand("kits").setExecutor(this);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Desligando o plugin HG Kits");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kits")) {
            sender.sendMessage(ChatColor.GREEN + "Kits disponíveis: Noob, Jackhammer");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser executado por um jogador.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Utilize o /kit <nome do kit>.");
            return true;
        }

        String kitName = args[0].toLowerCase();

        if (kitName.equals("jackhammer")) {
            JackhammerKit jackhammerKit = new JackhammerKit();
            jackhammerKit.giveKit(player);
            player.sendMessage(ChatColor.GREEN + "Você recebeu o kit Jackhammer.");
        } else if (kitName.equals("noob")) {
            NoobKit noobKit = new NoobKit();
            noobKit.giveKit(player);
            player.sendMessage(ChatColor.GREEN + "Você recebeu o kit Noob.");
        } else {
            player.sendMessage(ChatColor.RED +"Kit desconhecido. Use /kits para ver todos os kits.");
        }

        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand != null && itemInHand.getType() == Material.STONE_AXE &&
                itemInHand.getItemMeta() != null &&
                itemInHand.getItemMeta().getDisplayName().equals(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Jackhammer")) {
            event.setCancelled(true);

            if (!checkCooldown(player)) {
                breakColumn(event.getBlock());
                startCooldown(player, 3);
            } else {
                long remainingTime = getRemainingCooldown(player);
                player.sendMessage(ChatColor.RED + "Aguarde " + remainingTime + " segundos para utilizar novamente.");
            }
        }
    }

    private void breakColumn(Block block) {
        Block currentBlock = block;

        while (currentBlock.getY() > 0) {
            currentBlock.setType(Material.AIR);
            currentBlock = currentBlock.getRelative(0, -1, 0);
        }
    }

    private boolean checkCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long cooldownEnd = cooldowns.get(playerId);
            long currentTime = System.currentTimeMillis();
            return currentTime < cooldownEnd;
        }
        return false;
    }

    private void startCooldown(Player player, int seconds) {
        final UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long cooldownEnd = currentTime + (seconds * 1000);
        cooldowns.put(playerId, cooldownEnd);

        new BukkitRunnable() {
            public void run() {
                cooldowns.remove(playerId);
            }
        }.runTaskLater(this, seconds * 20);
    }

    private long getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long cooldownEnd = cooldowns.get(playerId);
            long currentTime = System.currentTimeMillis();
            long remainingTime = (cooldownEnd - currentTime) / 1000;
            return Math.max(0, remainingTime);
        }
        return 0;
    }
}
