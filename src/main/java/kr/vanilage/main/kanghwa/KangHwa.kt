package kr.vanilage.main.kanghwa

import kr.vanilage.main.InventoryGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import java.util.Random

class KangHwa : Listener {
    private val rd = Random()

    @EventHandler
    fun onClickEntity(e : PlayerInteractAtEntityEvent) {
        if (e.rightClicked.name.contains("§c대장장이")) {
            e.player.openInventory(InventoryGenerator().inventory)
        }
    }

    @EventHandler
    fun onClick(e : InventoryClickEvent) {
        if (!e.view.title.contains("§c아이템 강화")) return
        if (e.rawSlot < 27 && !(e.rawSlot == 10 || e.rawSlot == 16)) e.isCancelled = true
        if (e.rawSlot != 13) return

        val inv = e.inventory
        if (inv.getItem(10) == null || inv.getItem(16) == null) return

        if (isKangHwaAble(inv.getItem(10)!!.type) && inv.getItem(16)!!.displayName().toString().contains("§1강화된 청금석")) {
            val item = inv.getItem(10)!!
            val itemLore = item.lore
            var level = 0
            if (itemLore != null) {
                level = itemLore[0].length
            }

            if (level > 4) return

            when (level) {
                0 -> {
                    if (rd.nextInt(2) == 0) {
                        level++
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_PLAYER_LEVELUP, 100F, 2F)
                    }

                    else {
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_GENERIC_EXPLODE, 100F, 2F)
                    }
                }

                1 -> {
                    if (rd.nextInt(10) < 3) {
                        level++
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_PLAYER_LEVELUP, 100F, 2F)
                    }

                    else {
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_GENERIC_EXPLODE, 100F, 2F)
                    }
                }

                2 -> {
                    if (rd.nextInt(10) == 0) {
                        level++
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_PLAYER_LEVELUP, 100F, 2F)
                    }

                    else {
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_GENERIC_EXPLODE, 100F, 2F)
                    }
                }

                3 -> {
                    if (rd.nextInt(20) == 0) {
                        level++
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_PLAYER_LEVELUP, 100F, 2F)
                    }

                    else {
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_GENERIC_EXPLODE, 100F, 2F)
                    }
                }

                4 -> {
                    if (rd.nextInt(100) == 0) {
                        level++
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_PLAYER_LEVELUP, 100F, 2F)
                    }

                    else {
                        (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.ENTITY_GENERIC_EXPLODE, 100F, 2F)
                    }
                }
            }

            if (level != 0) {
                val lore = listOf("★".repeat(level))
                item.lore = lore
            }

            inv.getItem(16)!!.amount--
        }
    }

    @EventHandler
    fun onClose(e : InventoryCloseEvent) {
        if (e.view.title.contains("§c아이템 강화")) {
            if (e.inventory.getItem(10) != null) e.player.inventory.addItem(e.inventory.getItem(10)!!)
            if (e.inventory.getItem(16) != null) e.player.inventory.addItem(e.inventory.getItem(16)!!)
        }
    }

    @EventHandler
    fun onDamage(e : EntityDamageEvent) {
        if (e.entity.name.contains("대장장이")) e.isCancelled = true
    }

    private fun isKangHwaAble(type : Material) : Boolean {
        return type.name.endsWith("_SWORD") || type.name.endsWith("_AXE")
    }
}