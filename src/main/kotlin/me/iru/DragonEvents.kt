package me.iru

import net.md_5.bungee.api.ChatColor
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Damageable
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntitySpawnEvent

class DragonEvents : Listener {

    private val simpleDragonLevels = SimpleDragonLevels.instance

    private val mainColor = Utils.parseColor(simpleDragonLevels.config.getString("colors.main"))
    private val separatorColor = Utils.parseColor(simpleDragonLevels.config.getString("colors.separator"))
    private val levelColor = Utils.parseColor(simpleDragonLevels.config.getString("colors.level"))

    @EventHandler
    fun onDragonSpawn(e: EntitySpawnEvent) {
        if(e.entity is EnderDragon) {
            val dragon = e.entity
            val level = SimpleDragonLevels.data.get("level")
            val hp = Utils.calcDragonHealth( level as Int)
            (dragon as Attributable).getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue =
                hp.toDouble()
            (dragon as Damageable).health = hp.toDouble()
            dragon.customName = "${mainColor}Ender Dragon ${separatorColor}| ${ChatColor.WHITE}Level ${levelColor}${level}${ChatColor.RESET}"
            val end = dragon.location.world
            val k = "${if(level > 0) "re" else ""}spawned"
            for(p : Player in SimpleDragonLevels.instance.server.onlinePlayers) {
                val pLoc = p.location
                if(pLoc.world == end && Utils.isOnMainIsland(p)) {
                    p.sendTitle("${separatorColor}| ${mainColor}Ender Dragon ${ChatColor.WHITE}${k} ${separatorColor}|", "Level ${levelColor}${level} ${separatorColor}| ${ChatColor.WHITE}HP ${levelColor}${hp}", 10, 70, 20)
                } else {
                    p.sendMessage("${mainColor}${ChatColor.BOLD}[!] ${separatorColor}The ${mainColor}Ender Dragon ${separatorColor}has ${k}${mainColor}${ChatColor.BOLD}!")
                }
            }
        }
    }

    @EventHandler
    fun onDragonDeath(e: EntityDeathEvent) {
        if(e.entity is EnderDragon) {
            val current = SimpleDragonLevels.data.get("level") as Int

            val items = Utils.getFullItemDrops(current)
            for(item in items) {
                e.entity.world.dropItem(e.entity.location, item)
            }

            val xp = Utils.getFullXpDrops(current)
            if(xp != null) {
                e.droppedExp = e.droppedExp + xp
            }

            SimpleDragonLevels.data.set("level", current + 1)
        }
    }
}