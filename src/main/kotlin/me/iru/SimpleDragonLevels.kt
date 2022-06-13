package me.iru

import net.md_5.bungee.api.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class SimpleDragonLevels : JavaPlugin() {
    private val version = this.description.version
    companion object {
        lateinit var instance: JavaPlugin
        lateinit var data: DataManager
    }

    override fun onEnable() {
        instance = this

        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${this.name}${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Enabling $version")

        data = DataManager()
        server.pluginManager.registerEvents(DragonEvents(), this)
        saveDefaultConfig()
    }

    override fun onDisable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${this.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}Disabling $version")
    }
}