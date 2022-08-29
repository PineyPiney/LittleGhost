package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.objects.menu_items.TextButton
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2

class OptionsMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen("options menu.png", parent, gameEngine) {

    private val graphics = TextButton("Graphics", Vec2(-0.4, 0.6), Vec2(0.8, 0.3), window){
        setMenu(GraphicsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    private val audio = TextButton("Audio", Vec2(-0.4, 0.2), Vec2(0.8, 0.3), window){
        setMenu(AudioMenuScreen(this, gameEngine), false)
        openMenu()
    }

    val keys = TextButton("KeyBinds", Vec2(-0.4, -0.2), Vec2(0.8, 0.3), window){
        setMenu(KeyBindsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), Vec2(0.8, 0.3), window){
        setMenu(this.parent)
        openMenu(false)
    }

    override fun cleanUp() {
        super.cleanUp()
        UserSettings.saveOptions()
    }

    override fun addObjects() {
        super.addObjects()

        add(graphics)
        add(audio)
        add(keys)
        add(backButton)
    }
}