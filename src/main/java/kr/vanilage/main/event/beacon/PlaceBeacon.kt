package kr.vanilage.main.event.beacon

import kr.vanilage.main.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import java.util.UUID

class PlaceBeacon : Listener {
    @EventHandler
    fun onPlace(e : BlockPlaceEvent) {
        if (e.block.type == Material.REINFORCED_DEEPSLATE) {
            if (Main.instance.config.getConfigurationSection("team") != null) {
                Main.instance.config.getConfigurationSection("team")!!.getKeys(false).forEach {
                    for (i in Main.instance.config.getConfigurationSection("team.$it.member")!!.getKeys(false)) {
                        if (i == e.player.uniqueId.toString()) {
                            Main.instance.config.set("beacon.${e.block.x} ${e.block.y} ${e.block.z}.team", it)
                            Main.instance.saveConfig()

                            for (j in Main.instance.config.getConfigurationSection("team.$it.member")!!.getKeys(false)) {
                                if (Bukkit.getPlayer(UUID.fromString(j)) != null) {
                                    Bukkit.getPlayer(UUID.fromString(j))!!.sendMessage("§a신호기가 설치되었습니다.")
                                    Bukkit.getPlayer(UUID.fromString(j))!!.playSound(Bukkit.getPlayer(UUID.fromString(j))!!.location, Sound.ITEM_TOTEM_USE, 100F, 1F)
                                }
                            }

                            return
                        }
                    }
                }
            }

            e.player.sendMessage("§c팀이 없습니다.")
            e.isCancelled = true
        }
    }
}