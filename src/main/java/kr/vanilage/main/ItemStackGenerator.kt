package kr.vanilage.main

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackGenerator {
    companion object {
        fun generate(type : Material, displayName : Component) : ItemStack {
            val stack = ItemStack(type)
            val meta = stack.itemMeta
            meta.displayName(displayName)
            stack.itemMeta = meta
            return stack
        }
    }
}