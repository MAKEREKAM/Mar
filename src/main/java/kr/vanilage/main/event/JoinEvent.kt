package kr.vanilage.main.event

import kr.vanilage.main.Main
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent : Listener {
    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {
        if (Main.instance.config.getConfigurationSection("team") != null) {
            for (i in Main.instance.config.getConfigurationSection("team")!!.getKeys(false)) {
                for (j in Main.instance.config.getConfigurationSection("team.$i.member")!!.getKeys(false)) {
                    if (j == e.player.uniqueId.toString()) {
                        e.player.playerListName(Component.text("${e.player.name} [$i]"))
                    }
                }
            }
        }
    }
}