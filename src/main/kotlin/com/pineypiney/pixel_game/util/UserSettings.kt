package com.pineypiney.pixel_game.util

import com.pineypiney.pixel_game.LittleLogic
import com.pineypiney.pixel_game.LittleWindow
import com.pineypiney.pixel_game.audio.AudioType
import com.pineypiney.pixel_game.gameEngine
import glm_.f
import glm_.i
import glm_.vec2.Vec2i
import org.lwjgl.openal.AL10
import java.io.File

class UserSettings{

    companion object{
        var matchWindow = true
        var resolution: Vec2i = Vec2i(1920, 1080)
            get() = if(matchWindow) LittleWindow.INSTANCE.size else field

        var masterVolume
            get() = AL10.alGetListenerf(AL10.AL_GAIN)
            set(value) = AL10.alListenerf(AL10.AL_GAIN, value)

        var musicVolume
            get() = AudioType.MUSIC.value
            set(value) = setVolume(AudioType.MUSIC, value)
        var sfxVolume
            get() = AudioType.SFX.value
            set(value) = setVolume(AudioType.SFX, value)

        fun setVolume(type: AudioType, value: Float){
            type.value = value
            (gameEngine.activeScreen as LittleLogic).updateVolume()
        }

        fun loadOptions(){
            val file = File("config.txt")
            if(file.exists()){
                for(line in file.readLines()){
                    try{
                        val (option, value) = line.split(':')
                        when(option){
                            "Resolution" -> {
                                matchWindow = value == "window"
                                if(!matchWindow) resolution = Vec2i(0, value.split(' ').map { it.i }.toIntArray())
                            }
                            "Master Volume" -> masterVolume = value.f
                            "Music Volume" -> musicVolume = value.f
                            "SFX Volume" -> sfxVolume = value.f
                        }
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }

                }
            }
        }

        fun saveOptions(){
            val file = File("config.txt")
            file.createNewFile()

            val text = StringBuilder()

            text.appendLine("Resolution:${if(matchWindow) "window" else resolution.run { "$x $y" }}")
            text.appendLine("Master Volume:$masterVolume")
            text.appendLine("Music Volume:$musicVolume")
            text.appendLine("SFX Volume:$sfxVolume")

            file.writeText(text.toString())
        }
    }
}
