package br.com.bernardo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public final class Plugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Iniciando o plugin");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Desligando o plugin");
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            Entity hitEntity = null;

            List<Entity> entities = event.getEntity().getNearbyEntities(1, 1, 1);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && entity != shooter) {
                    hitEntity = entity;
                    break;
                }
            }

            if (hitEntity != null) {
                LivingEntity livingEntity = (LivingEntity) hitEntity;

                Vector shooterLocation = shooter.getLocation().toVector();
                Vector entityLocation = livingEntity.getLocation().toVector();

                shooter.teleport(entityLocation.toLocation(shooter.getWorld()));
                livingEntity.teleport(shooterLocation.toLocation(livingEntity.getWorld()));
            }
        }
    }
}
