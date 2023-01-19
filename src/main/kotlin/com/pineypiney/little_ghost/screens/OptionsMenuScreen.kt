package com.pineypiney.little_ghost.screens

import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2

class OptionsMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen(parent, gameEngine) {

    private val graphics = TextButton("Graphics", Vec2(-0.4, 0.6), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(GraphicsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    private val audio = TextButton("Audio", Vec2(-0.4, 0.2), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(AudioMenuScreen(this, gameEngine), false)
        openMenu()
    }

    val keys = TextButton("KeyBinds", Vec2(-0.4, -0.2), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(KeyBindsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), 0.3f, 0xF5E5DF, 0.8f){
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