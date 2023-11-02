package kr.vanilage.main.event

import org.bukkit.Material
import org.bukkit.entity.Skeleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.inventory.ItemStack
import java.util.Random

class SpawnEvent : Listener {
    val rd = Random()

    @EventHandler
    fun onSpawn(e : EntitySpawnEvent) {
        if (e.entity is Skeleton) {
            if (rd.nextInt(200) == 0) {
                (e.entity as Skeleton).equipment.helmet = ItemStack(Material.WITHER_SKELETON_SKULL)
            }
        }
    }
}