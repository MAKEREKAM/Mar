package kr.vanilage.main.event

import kr.vanilage.main.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnEvent : Listener {
    @EventHandler
    fun onRespawn(e : PlayerRespawnEvent) {
        val p = e.player
        if (Main.instance.config.getConfigurationSection("team") != null) {
            for (i in Main.instance.config.getConfigurationSection("team")!!.getKeys(false)) {
                for (j in Main.instance.config.getConfigurationSection("team.$i.member")!!.getKeys(false)) {
                    if (j == p.uniqueId.toString()) {
                        for (k in Main.instance.config.getConfigurationSection("beacon")!!.getKeys(false)) {
                            if (Main.instance.config.getString("beacon.$k.team").equals(i)) {
                                val x = k.split(" ")[0].toDouble()
                                val y = k.split(" ")[1].toDouble()
                                val z = k.split(" ")[2].toDouble()
                                e.setRespawnLocation(Location(Bukkit.getWorld("world"), x + 0.5, y, z + 0.5))
                            }
                        }
                    }
                }
            }
        }
    }
}