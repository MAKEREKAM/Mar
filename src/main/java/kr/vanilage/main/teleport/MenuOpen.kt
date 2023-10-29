package kr.vanilage.main.teleport

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class MenuOpen : Listener {
    @EventHandler
    fun onSwap(e : PlayerSwapHandItemsEvent) {
        if (e.player.isSneaking) {
            e.isCancelled = true
            InventoryOpener.openInventory(e.player)
        }
    }
}