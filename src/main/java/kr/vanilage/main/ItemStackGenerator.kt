package kr.vanilage.main

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackGenerator {
    companion object {
        fun generate(type : Material, displayName : String) : ItemStack {
            val stack = ItemStack(type)
            val meta = stack.itemMeta
            meta.setDisplayName(displayName)
            stack.itemMeta = meta
            return stack
        }
    }
}