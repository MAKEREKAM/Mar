package kr.vanilage.main.event.beacon

import kr.vanilage.main.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import java.util.*


class BreakBeacon : Listener {
    @EventHandler
    fun onBreak(e : BlockBreakEvent) {
        if (Main.instance.config.getConfigurationSection("beacon") != null) {
            Main.instance.config.getConfigurationSection("beacon")!!.getKeys(false).forEach {
                val beaconX = it.split(" ")[0].toInt()
                val beaconY = it.split(" ")[1].toInt()
                val beaconZ = it.split(" ")[2].toInt()
                val x = e.block.x
                val y = e.block.y
                val z = e.block.z
                if (beaconX == x && beaconY == y && beaconZ == z) {
                    val team = Main.instance.config.getString("beacon.$it.team")
                    Main.instance.config.set("beacon.$it", null)
                    Main.instance.saveConfig()

                    for (i in Main.instance.config.getConfigurationSection("team.$team.member")!!.getKeys(false)) {
                        if (Bukkit.getPlayer(UUID.fromString(i)) != null) Bukkit.getPlayer(UUID.fromString(i))!!.sendMessage("§c신호기가 파괴되었습니다.")
                    }

                    for (i in Main.instance.config.getConfigurationSection("beacon")!!.getKeys(false)) {
                        if (Main.instance.config.getString("beacon.$i.team") == team) return
                    }

                    Bukkit.broadcast(Component.text("$team 팀이 멸망했습니다.", NamedTextColor.RED))

                    for (i in Main.instance.config.getConfigurationSection("team.$team.member")!!.getKeys(false)) {
                        Main.instance.config.set("final.$i", 0)
                    }

                    Main.instance.config.set("team.$team", null)
                    Main.instance.saveConfig()
                }
            }
        }
    }
}