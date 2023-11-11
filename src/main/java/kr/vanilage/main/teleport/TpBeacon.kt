package kr.vanilage.main.teleport

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class TpBeacon : Listener {
    @EventHandler
    fun onClickInventory(e : InventoryClickEvent) {
        if (e.rawSlot > 53) return
        if (!e.view.title.contains("§c신호기로 텔레포트하기")) return
        e.isCancelled = true
        if (e.currentItem == null) return

        val location = e.currentItem!!.itemMeta.displayName
        val x = location.split(" ")[0].toDouble()
        val y = location.split(" ")[1].toDouble()
        val z = location.split(" ")[2].toDouble()

        e.whoClicked.teleport(Location(Bukkit.getWorld("world"), x + 0.5, y + 1, z + 0.5))
    }
}