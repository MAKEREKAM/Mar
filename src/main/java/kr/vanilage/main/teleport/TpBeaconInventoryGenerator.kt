package kr.vanilage.main.teleport

import kr.vanilage.main.InventoryGenerator
import kr.vanilage.main.ItemStackGenerator
import kr.vanilage.main.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class TpBeaconInventoryGenerator(player : Player) : InventoryHolder {
    private var inv : Inventory = Bukkit.createInventory(this, 54, "§c신호기로 텔레포트하기")

    init {
        var team = ""
        if (Main.instance.config.getConfigurationSection("team") != null) {
            for (i in Main.instance.config.getConfigurationSection("team")!!.getKeys(false)) {
                for (j in Main.instance.config.getConfigurationSection("team.$i.member")!!.getKeys(false)) {
                    if (j == player.uniqueId.toString()) team = i
                }
            }
        }

        if (Main.instance.config.getConfigurationSection("beacon") != null) {
            for (i in Main.instance.config.getConfigurationSection("beacon")!!.getKeys(false)) {
                if (Main.instance.config.getString("beacon.$i.team") == team) {
                    val item = ItemStackGenerator.generate(Material.REINFORCED_DEEPSLATE, i)
                    inv.addItem(item)
                }
            }
        }
    }

    override fun getInventory(): Inventory {
        return inv
    }
}