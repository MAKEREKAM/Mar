package kr.vanilage.main.event

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

class EntityDamageEvent : Listener {
    @EventHandler
    fun onDamage(e : EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Skeleton) {
            if ((e.entity as Skeleton).equipment.helmet != null) {
                if ((e.entity as Skeleton).equipment.helmet.type == Material.WITHER_SKELETON_SKULL) {
                    (e.entity as Skeleton).remove()
                    e.entity.world.dropItemNaturally(e.entity.location, ItemStack(Material.WITHER_SKELETON_SKULL))
                }
            }
        }
    }
}