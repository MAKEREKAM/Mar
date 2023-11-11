package kr.vanilage.main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class InventoryGenerator : InventoryHolder {
    private lateinit var inv : Inventory

    init {
        inv = Bukkit.createInventory(this, 27, "§c아이템 강화")
        for (i in 0..26) {
            inv.setItem(i, ItemStackGenerator.generate(Material.BLACK_STAINED_GLASS_PANE, ""))
        }

        inv.setItem(10, ItemStack(Material.AIR))
        inv.setItem(13, ItemStackGenerator.generate(Material.ANVIL, "§c강화하기"))
        inv.setItem(16, ItemStack(Material.AIR))
    }

    override fun getInventory() : Inventory {
        return inv
    }
}