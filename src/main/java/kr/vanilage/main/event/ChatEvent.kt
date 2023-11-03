package kr.vanilage.main.event

import kr.vanilage.main.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID

class ChatEvent : Listener {
    @EventHandler
    fun onChat(e : AsyncPlayerChatEvent) {
        if (Main.teamChat.contains(e.player.uniqueId)) {
            e.isCancelled = true
            if (Main.instance.config.getConfigurationSection("team") != null) {
                var team = ""
                for (i in Main.instance.config.getConfigurationSection("team")!!.getKeys(false)) {
                    for (j in Main.instance.config.getConfigurationSection("team.$i.member")!!.getKeys(false)) {
                        if (j == e.player.uniqueId.toString()) team = i
                    }
                }

                if (team == "") return
                for (i in Main.instance.config.getConfigurationSection("team.$team.member")!!.getKeys(false)) {
                    if (Bukkit.getPlayer(UUID.fromString(i)) != null) {
                        Bukkit.getPlayer(UUID.fromString(i))!!.sendMessage("[팀채팅] ${e.player.name} : ${e.message}")
                    }
                }
            }
        }
    }
}