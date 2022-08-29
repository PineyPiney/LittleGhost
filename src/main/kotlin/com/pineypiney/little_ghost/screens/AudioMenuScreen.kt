package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.objects.menu_items.TextButton
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.MenuSlider
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2

class AudioMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen("audio menu", parent, gameEngine) {

    val masterVolume = MenuSlider(Vec2(-0.4, 0.85), Vec2(0.8, 0.1), 0f, 200f, UserSettings.masterVolume * 100){
        UserSettings.masterVolume = it / 100
    }

    val musicVolume = MenuSlider(Vec2(-0.4, 0.65), Vec2(0.8, 0.1), 0f, 100f, UserSettings.musicVolume * 100){
        UserSettings.musicVolume = it / 100
    }

    val sfxVolume = MenuSlider(Vec2(-0.4, 0.45), Vec2(0.8, 0.1), 0f, 100f, UserSettings.sfxVolume * 100){
        UserSettings.sfxVolume = it / 100
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), Vec2(0.8, 0.3), window){
        setMenu(this.parent)
        openMenu(false)
    }

    override fun addObjects() {
        super.addObjects()

        add(masterVolume)
        add(musicVolume)
        add(sfxVolume)
        add(backButton)
    }
}