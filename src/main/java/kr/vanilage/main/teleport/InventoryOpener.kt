package kr.vanilage.main.teleport

import io.github.monun.invfx.InvFX
import io.github.monun.invfx.openFrame
import kr.vanilage.main.ItemStackGenerator
import kr.vanilage.main.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class InventoryOpener {
    companion object {
        fun openInventory(player : Player) {
            val frame = InvFX.frame(3, Component.text("텔레포트", NamedTextColor.DARK_BLUE)) {
                slot(1, 1) {
                    item = ItemStackGenerator.generate(Material.REINFORCED_DEEPSLATE, Component.text("팀의 첫 신호기로 이동", NamedTextColor.RED))
                    onClick {
                        tpBeacon(it.whoClicked as Player)
                    }
                }

                slot(3, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_PLANKS, Component.text("마을로 이동", NamedTextColor.YELLOW))
                    onClick {
                        tpVillage(it.whoClicked as Player)
                    }
                }

                slot(5, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, Component.text("텔레포트 포인트 #1로 이동", NamedTextColor.GREEN))
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "1")
                    }
                }

                slot(6, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, Component.text("텔레포트 포인트 #2로 이동", NamedTextColor.GREEN))
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "2")
                    }
                }

                slot(7, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, Component.text("텔레포트 포인트 #3으로 이동", NamedTextColor.GREEN))
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "3")
                    }
                }
            }

            player.openFrame(frame)
        }

        fun tpBeacon(player : Player) {
            if (Main.instance.config.getConfigurationSection("team") != null) {
                for (i in Main.instance.config.getConfigurationSection("team")!!.getKeys(false)) {
                    for (j in Main.instance.config.getConfigurationSection("team.$i.member")!!.getKeys(false)) {
                        if (j == player.uniqueId.toString()) {
                            for (k in Main.instance.config.getConfigurationSection("beacon")!!.getKeys(false)) {
                                if (Main.instance.config.getString("beacon.$k.team").equals(i)) {
                                    val x = k.split(" ")[0].toDouble()
                                    val y = k.split(" ")[1].toDouble()
                                    val z = k.split(" ")[2].toDouble()
                                    player.teleport(Location(Bukkit.getWorld("world"), x + 0.5, y + 1.0, z + 0.5))
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }

        fun tpVillage(player : Player) {
            val y = Bukkit.getWorld("world")!!.getHighestBlockYAt(0, 0)
            player.teleport(Location(Bukkit.getWorld("world"), 0.5, y.toDouble() + 1, 0.5))
        }

        fun tpTeleportPoint(player : Player, point : String) {
            if (Main.instance.config.getString("tp${point}.${player.uniqueId.toString()}") != null) {
                val point = Main.instance.config.getString("tp${point}.${player.uniqueId.toString()}")!!.split(" ")
                val x = point[0].toDouble() + 0.5
                val y = point[1].toDouble()
                val z = point[2].toDouble() + 0.5

                player.teleport(Location(Bukkit.getWorld("world"), x, y, z))
            }
        }
    }
}