package me.iru

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.Exception
import java.util.regex.Pattern

object Utils {
    fun calcDragonHealth(level: Int): Int {
        var h = 200 + (level * (SimpleDragonLevels.instance.config.getInt("multiplier")))
        if(h > 2048) h = 2048
        return h
    }

    fun isOnMainIsland(p: Player): Boolean {
        val pX = p.location.blockX
        val pZ = p.location.blockZ
        return (pX >= -100 && pX <= 100) && (pZ >= -100 && pZ <= 100)
    }

    private fun getDropsForLevel(l: Any): Drop? {
        val drops = getDrops()
        if(!areDropsValid(drops)) {
            throw Exception("There can be only one drop entry for each level!")
        }
        drops.removeIf {
            it.level != l
        }
        return if (drops.isEmpty()) null else drops[0]
    }

    private fun getItemDropsForLevel(l: Any): MutableList<ItemStack>? {
        val drop = getDropsForLevel(l)
        val items = mutableListOf<ItemStack>()
        if(drop?.items == null) return null
        drop.items.forEach {
            val mat = Material.valueOf(it)
            items.add(ItemStack(mat))
        }
        return items
    }


    private fun getXpDropsForLevel(l: Any): Int? {
        val drop = getDropsForLevel(l)
        return drop?.xp
    }

    fun getFullXpDrops(l: Int): Int? {
        val xpAll = getXpDropsForLevel("all") ?: 0
        val xpLevel = getXpDropsForLevel(l) ?: 0
        val xp = xpAll + xpLevel
        return if(xp == 0) null else xp
    }

    fun getFullItemDrops(l: Int): MutableList<ItemStack> {
        val items = mutableListOf<ItemStack>()
        val itemsAll = getItemDropsForLevel("all")
        val itemsLevel = getItemDropsForLevel(l)
        if (itemsAll != null) {
            for (item in itemsAll) {
                items.add(item)
            }
        }
        if (itemsLevel != null) {
            for(item in itemsLevel) {
                items.add(item)
            }
        }
        return items
    }

    @Suppress("UNCHECKED_CAST")
    fun getDrops(): MutableList<Drop> {
        val config = SimpleDragonLevels.instance.config
        val r = config.getList("drops", mutableListOf<LinkedHashMap<Any, Any>>())
        return createDrops(r as MutableList<LinkedHashMap<Any, Any>>?)
    }

    private fun areDropsValid(d: MutableList<Drop>): Boolean {
        val checked = mutableListOf<Any>()
        for(drop in d) {
            if(checked.contains(drop.level)) return false
            checked.add(drop.level)
        }
        return true
    }

    @Suppress("UNCHECKED_CAST")
    private fun createDrops(c: MutableList<LinkedHashMap<Any, Any>>?): MutableList<Drop> {
        val r = mutableListOf<Drop>()
        if (c != null) {
            for(g in c) {
                val level = g["level"] ?: throw Exception("No level specified in drop! Check config!")
                r.add(Drop(level, g["items"] as MutableList<String>?, g["xp"] as Int?))
            }
        }
        return r
    }

    fun parseColor(value: String?): ChatColor {
        if (value == null) {
            return ChatColor.BLACK
        }
        val mcRegex = Pattern.compile("^&([0-9a-f])\$")
        val hexRegex = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")
        return if(hexRegex.matcher(value).matches()) {
            ChatColor.of(value)
        } else if(mcRegex.matcher(value).matches()) {
            ChatColor.getByChar(value[1])
        } else {
            ChatColor.BLACK
        }
    }
}