package kr.vanilage.main.event

import kr.vanilage.main.Main
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

class KillEvent : Listener {
    @EventHandler
    fun onDeath(e : PlayerDeathEvent) {
        if (Main.instance.config.getConfigurationSection("final") == null) return
        if (!Main.instance.config.getConfigurationSection("final")!!.getKeys(false).contains(e.player.uniqueId.toString())) return
        var damager : Entity? = null

        val entityDamageEvent = e.entity.lastDamageCause
        if (entityDamageEvent != null && !entityDamageEvent.isCancelled && entityDamageEvent is EntityDamageByEntityEvent) {
            damager = entityDamageEvent.damager

            if (damager is Projectile) {
                damager = damager.shooter as LivingEntity
            }
        }

        if (damager != null && damager is Player) {
            if (Main.instance.config.getConfigurationSection("team") != null) {
                Main.instance.config.getConfigurationSection("team")!!.getKeys(false).forEach {
                    for (i in Main.instance.config.getConfigurationSection("team.$it.member")!!.getKeys(false)) {
                        if (i == damager.uniqueId.toString()) {
                            Main.instance.config.set("final.${e.player.uniqueId.toString()}", null)
                            Main.instance.config.set("team.$it.member.${e.player.uniqueId.toString()}", 0)
                            return
                        }
                    }
                }
            }
        }
    }
}