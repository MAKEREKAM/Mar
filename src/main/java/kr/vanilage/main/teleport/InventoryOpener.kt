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

class InventoryOpener {
    companion object {
        fun openInventory(player : Player) {
            val frame = InvFX.frame(5, Component.text("텔레포트", NamedTextColor.DARK_BLUE)) {
                slot(1, 2) {
                    item = ItemStackGenerator.generate(Material.REINFORCED_DEEPSLATE, "§c팀의 신호기로 이동")
                    onClick {
                        it.whoClicked.closeInventory()
                        tpBeacon(it.whoClicked as Player)
                    }
                }

                slot(3, 2) {
                    item = ItemStackGenerator.generate(Material.OAK_PLANKS, "§e마을로 이동")
                    onClick {
                        tpVillage(it.whoClicked as Player)
                    }
                }

                slot(5, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #1로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "1")
                    }
                }

                slot(6, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #2로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "2")
                    }
                }

                slot(7, 1) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #3으로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "3")
                    }
                }

                slot(5, 2) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #4로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "4")
                    }
                }

                slot(6, 2) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #5로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "5")
                    }
                }

                slot(7, 2) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #6으로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "6")
                    }
                }

                slot(5, 3) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #7로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "7")
                    }
                }

                slot(6, 3) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #8로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "8")
                    }
                }

                slot(7, 3) {
                    item = ItemStackGenerator.generate(Material.OAK_SIGN, "§a텔레포트 포인트 #9로 이동")
                    onClick {
                        tpTeleportPoint(it.whoClicked as Player, "9")
                    }
                }
            }

            player.openFrame(frame)
        }

        private fun tpBeacon(player : Player) {
            player.openInventory(TpBeaconInventoryGenerator(player).inventory)
        }

        private fun tpVillage(player : Player) {
            val y = Bukkit.getWorld("world")!!.getHighestBlockYAt(0, 0)
            player.teleport(Location(Bukkit.getWorld("world"), 0.5, y.toDouble() + 1, 0.5))
        }

        private fun tpTeleportPoint(player : Player, point : String) {
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