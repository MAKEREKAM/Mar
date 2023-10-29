package kr.vanilage.main.kanghwa

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class KangHwaSkill : Listener {
    @EventHandler
    fun onDamage(e : EntityDamageByEntityEvent) {
        if (e.damager is Player) {
            if ((e.damager as Player).inventory.itemInMainHand.lore != null) {
                val level = (e.damager as Player).inventory.itemInMainHand.lore!![0].length
                e.damage += level
            }
        }
    }
}