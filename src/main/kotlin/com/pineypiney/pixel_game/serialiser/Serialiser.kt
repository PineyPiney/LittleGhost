package com.pineypiney.pixel_game.serialiser

import com.pineypiney.game_engine.objects.game_objects.GameObject
import com.pineypiney.pixel_game.LittleEngine
import java.io.File

abstract class Serialiser<E: GameObject>(val fileName: String) {

    fun saveObjects(worldName: String, string: String){
        val file = File("$dataDirectory\\$worldName\\$fileName.txt")
        if(!file.exists()) {
            LittleEngine.logger.info("Abs Path: ${file.parentFile.path}")
            if (!file.parentFile.mkdirs()) {
                LittleEngine.logger.warn("Couldn't create directories needed to save game")
            }
            if (!file.createNewFile()) {
                LittleEngine.logger.warn("Couldn't create Save File")
                return
            }
        }
        file.writeText(string)



    }

    companion object {
        val dataDirectory = "data"
    }

}