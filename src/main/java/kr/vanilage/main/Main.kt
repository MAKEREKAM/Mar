package kr.vanilage.main

import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import kr.vanilage.main.event.*
import kr.vanilage.main.event.beacon.BreakBeacon
import kr.vanilage.main.event.beacon.PlaceBeacon
import kr.vanilage.main.kanghwa.KangHwa
import kr.vanilage.main.kanghwa.KangHwaSkill
import kr.vanilage.main.teleport.MenuOpen
import kr.vanilage.main.teleport.TpBeacon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Main : JavaPlugin() {
    val rd = Random()

    override fun onEnable() {
        this.saveDefaultConfig()
        instance = this

        Bukkit.getPluginManager().registerEvents(JoinEvent(), this)
        Bukkit.getPluginManager().registerEvents(KillEvent(), this)
        Bukkit.getPluginManager().registerEvents(BreakBeacon(), this)
        Bukkit.getPluginManager().registerEvents(PlaceBeacon(), this)
        Bukkit.getPluginManager().registerEvents(KangHwa(), this)
        Bukkit.getPluginManager().registerEvents(KangHwaSkill(), this)
        Bukkit.getPluginManager().registerEvents(RespawnEvent(), this)
        Bukkit.getPluginManager().registerEvents(MenuOpen(), this)
        Bukkit.getPluginManager().registerEvents(SpawnEvent(), this)
        Bukkit.getPluginManager().registerEvents(ChatEvent(), this)
        Bukkit.getPluginManager().registerEvents(EntityDamageEvent(), this)
        Bukkit.getPluginManager().registerEvents(BlockSpawnPVP(), this)
        Bukkit.getPluginManager().registerEvents(TpBeacon(), this)

        kommand {
            register("팀") {
                requires { isPlayer }
                then("생성") {
                    then("teamName" to string()) {
                        executes {
                            val teamName : String by it

                            if (this@Main.config.getConfigurationSection("team") != null) {
                                this@Main.config.getConfigurationSection("team")!!.getKeys(false).forEach {
                                    if (teamName == it) {
                                        player.sendMessage("§c이미 해당 이름의 팀이 있습니다.")
                                        return@executes
                                    }

                                    for (i in this@Main.config.getConfigurationSection("team.$it.member")!!
                                        .getKeys(false)) {
                                        if (i == player.uniqueId.toString()) {
                                            player.sendMessage("§c이미 다른 팀의 구성원입니다.")
                                            return@executes
                                        }
                                    }
                                }
                            }

                            if (this@Main.config.getConfigurationSection("final") != null) {
                                if (this@Main.config.getConfigurationSection("final")!!.getKeys(false)
                                        .contains(player.getUniqueId().toString())
                                ) {
                                    player.sendMessage("§c팀의 신호기가 부서져 팀을 만들 수 없습니다.");
                                    return@executes
                                }
                            }

                            player.location.block.type = Material.REINFORCED_DEEPSLATE
                            this@Main.config.set("team.$teamName.leader", player.uniqueId.toString())
                            this@Main.config.set("team.$teamName.member.${player.uniqueId.toString()}", 0)
                            this@Main.config.set("beacon.${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}.team", teamName)
                            this@Main.saveConfig()
                            Bukkit.broadcast(Component.text("$teamName 팀이 생성되었습니다.", NamedTextColor.GREEN))
                            player.playerListName(Component.text("${player.name} [$teamName]"))

                            Bukkit.getOnlinePlayers().forEach {
                                it.playSound(it.location, Sound.ITEM_TOTEM_USE, 100F, 1F)
                            }
                        }
                    }
                }

                then("가입") {
                    then("teamName" to string()) {
                        executes {
                            val teamName : String by it

                            if (this@Main.config.getConfigurationSection("team") != null) {
                                this@Main.config.getConfigurationSection("team")!!.getKeys(false).forEach {
                                    for (i in this@Main.config.getConfigurationSection("team.$it.member")!!
                                        .getKeys(false)) {
                                        if (i == player.uniqueId.toString()) {
                                            player.sendMessage("§c이미 다른 팀의 구성원입니다.")
                                            return@executes
                                        }
                                    }
                                }
                            }

                            if (this@Main.config.getConfigurationSection("final") != null) {
                                if (this@Main.config.getConfigurationSection("final")!!.getKeys(false)
                                        .contains(player.uniqueId.toString())
                                ) {
                                    player.sendMessage("§c팀의 신호기가 부서져 다른 팀에 들어갈 수 없습니다.")
                                    return@executes
                                }
                            }


                            if (this@Main.config.getConfigurationSection("team.$teamName") == null) {
                                player.sendMessage("§c팀이 존재하지 않습니다.")
                                return@executes
                            }

                            this@Main.config.set("team.$teamName.member.${player.uniqueId.toString()}", 0)
                            this@Main.saveConfig()
                            player.playerListName(Component.text("${player.name} [$teamName]"))

                            for (i in this@Main.config.getConfigurationSection("team.$teamName.member")!!.getKeys(false)) {
                                if (Bukkit.getPlayer(UUID.fromString(i)) != null) {
                                    Bukkit.getPlayer(UUID.fromString(i))!!.sendMessage("§a${player.name}님이 팀에 합류했습니다.")
                                }
                            }
                        }
                    }
                }
            }

            register("대장장이") {
                requires { isPlayer && isOp }
                executes {
                    val entity = player.world.spawn(player.location, Villager::class.java)
                    entity.setAI(false)
                    entity.customName(Component.text("§c대장장이"))
                }
            }

            register("tp1") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp1.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp2") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp2.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp3") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp3.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp4") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp4.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp5") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp5.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp6") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp6.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp7") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp7.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp8") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp8.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("tp9") {
                requires { isPlayer }
                executes {
                    this@Main.config.set("tp9.${player.uniqueId.toString()}", "${player.location.blockX} ${player.location.blockY} ${player.location.blockZ}")
                    this@Main.saveConfig()
                    player.sendMessage("§a세팅 완료.")
                }
            }

            register("qwergfdsa") {
                requires { isOp }
                then("name" to string()) {
                    executes {
                        val name : String by it
                        val player = Bukkit.getPlayer(name)!!
                        val random = rd.nextInt(1, 101)
                        if (random <= 30) {
                            player.inventory.addItem(ItemStack(Material.IRON_INGOT, 10))
                            player.inventory.addItem(ItemStack(Material.GOLD_INGOT, 5))
                            player.inventory.addItem(ItemStack(Material.DIAMOND, 5))
                        }
                        else if (random <= 50) {
                            val kanghwaLapis = ItemStackGenerator.generate(Material.LAPIS_LAZULI, "§1강화된 청금석")
                            kanghwaLapis.amount = rd.nextInt(10, 16)
                            player.inventory.addItem(kanghwaLapis)
                        }
                        else if (random <= 70) {
                            player.inventory.addItem(ItemStack(Material.LODESTONE))
                        }
                        else if (random <= 80) {
                            player.inventory.addItem(ItemStack(Material.DIAMOND_HELMET))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_CHESTPLATE))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_LEGGINGS))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_BOOTS))
                        }
                        else if (random <= 90) {
                            player.inventory.addItem(ItemStack(Material.DIAMOND_SWORD))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_PICKAXE))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_SHOVEL))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_AXE))
                            player.inventory.addItem(ItemStack(Material.DIAMOND_HOE))
                        }
                        else if (random <= 95) {
                            player.inventory.addItem(ItemStack(Material.TRIDENT))
                        }
                        else if (random <= 98) {
                            player.inventory.addItem(ItemStack(Material.NETHER_STAR))
                        }
                        else if (random <= 99) {
                            player.inventory.addItem(ItemStack(Material.ELYTRA))
                        }

                        Bukkit.broadcast(Component.text("${name}님이 추천해 주셨습니다.", NamedTextColor.GREEN))
                    }
                }
            }

            register("teamchat") {
                requires { isPlayer }
                executes {
                    if (teamChat.contains(player.uniqueId)) {
                        teamChat.remove(player.uniqueId)
                        player.sendMessage("§a팀 채팅이 꺼졌습니다.")
                    }
                    else {
                        teamChat.add(player.uniqueId)
                        player.sendMessage("§a팀 채팅이 켜졌습니다.")
                    }
                }
            }

            register("gamestart") {
                requires { isConsole }
                executes {
                    this@Main.config.set("gamestart", "true")
                    this@Main.saveConfig()
                }
            }
        }

        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            if (this@Main.config.getConfigurationSection("beacon") != null) {
                for (i in this@Main.config.getConfigurationSection("beacon")!!.getKeys(false)) {
                    val loc = i.split(" ")
                    val x = loc[0].toDouble()
                    val y = loc[1].toDouble()
                    val z = loc[2].toDouble()

                    Bukkit.getWorld("world")!!.spawnParticle(
                        Particle.GLOW_SQUID_INK,
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        500,
                        0.0,
                        100.0,
                        0.0,
                        0.0,
                        null,
                        true
                    )
                }
            }
        }, 0, 1)

        val kanghwaLapis = ItemStackGenerator.generate(Material.LAPIS_LAZULI, "§1강화된 청금석")
        val kanghwaLapisRecipe = ShapedRecipe(NamespacedKey(this, "kanghwa_lapis"), kanghwaLapis)
        kanghwaLapisRecipe.shape(" A ", "ALA", " A ")
        kanghwaLapisRecipe.setIngredient('A', Material.GOLD_INGOT)
        kanghwaLapisRecipe.setIngredient('L', Material.LAPIS_LAZULI)

        val beacon = ItemStackGenerator.generate(Material.REINFORCED_DEEPSLATE, "§e신호기")
        val beaconRecipe = ShapelessRecipe(NamespacedKey(this, "beacon"), beacon)
        beaconRecipe.addIngredient(Material.BEACON)

        val soulSandRecipe = ShapelessRecipe(NamespacedKey(this, "soul_sand"), ItemStack(Material.SOUL_SAND))
        soulSandRecipe.addIngredient(Material.SAND)
        soulSandRecipe.addIngredient(Material.COAL)

        Bukkit.addRecipe(kanghwaLapisRecipe)
        Bukkit.addRecipe(beaconRecipe)
        Bukkit.addRecipe(soulSandRecipe)
    }

    companion object {
        lateinit var instance : JavaPlugin
        val teamChat = ArrayList<UUID>()
    }
}