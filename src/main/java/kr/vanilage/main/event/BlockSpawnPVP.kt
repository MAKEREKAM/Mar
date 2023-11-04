package kr.vanilage.main.event

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.abs

class BlockSpawnPVP : Listener {
    @EventHandler
    fun onDamage(e : EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Player) {
            val a = abs(e.damager.location.blockX)
            val b = abs(e.damager.location.blockZ)
            val c = abs(e.entity.location.blockX)
            val d = abs(e.entity.location.blockZ)

            if (a <= 50 && b <= 50 && c <= 50 && d <= 50) {
                e.isCancelled = true
            }
        }
    }
}