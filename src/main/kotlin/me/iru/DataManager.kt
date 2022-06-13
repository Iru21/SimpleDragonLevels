package me.iru

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataManager {

    private val data = File(SimpleDragonLevels.instance.dataFolder, "data.yml")

    init {
        SimpleDragonLevels.instance.dataFolder.mkdir()
        if(!data.exists()) {
            data.createNewFile()
            val temp = YamlConfiguration.loadConfiguration(data)
            temp.set("level", 0)
            temp.save(data)
        }
    }

    fun get(key : String) : Any? {
        val temp = YamlConfiguration.loadConfiguration(data)
        return temp.get(key)
    }

    fun set(key : String, _val: Any) {
        val temp = YamlConfiguration.loadConfiguration(data)
        temp.set(key, _val)
        temp.save(data)
    }
}