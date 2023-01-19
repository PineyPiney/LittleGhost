package com.pineypiney.little_ghost.screens

import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2
import glm_.vec2.Vec2i

class GraphicsMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen(parent, gameEngine) {

    val windowRes = TextButton("Window Resolution", Vec2(-0.4, 0.85), 0.1f, 0xF5E5DF, 0.8f){
        UserSettings.matchWindow = true
        updateBuffers()
    }

    val FHDRes = TextButton("1920 x 1080", Vec2(-0.4, 0.7), 0.1f, 0xF5E5DF, 0.8f){
        setResolution(Vec2i(1920, 1080))
    }

    val HDRes = TextButton("1280 x 720", Vec2(-0.4, 0.55), 0.1f, 0xF5E5DF, 0.8f){
        setResolution(Vec2i(1280, 720))
    }

    val four80 = TextButton("640 x 480", Vec2(-0.4, 0.4), 0.1f, 0xF5E5DF, 0.8f){
        setResolution(Vec2i(640, 480))
    }

    val two40 = TextButton("320 x 240", Vec2(-0.4, 0.25), 0.1f, 0xF5E5DF, 0.8f){
        setResolution(Vec2i(320, 240))
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(this.parent)
        openMenu(false)
    }

    override fun addObjects() {
        super.addObjects()

        add(windowRes)
        add(FHDRes)
        add(HDRes)
        add(four80)
        add(two40)
        add(backButton)
    }

    private fun setResolution(res: Vec2i){
        UserSettings.resolution = res
        UserSettings.matchWindow = false
        updateBuffers()
    }

    fun updateBuffers(){
        gameEngine.game.regenerateFrameBuffers()
        gameEngine.menu.regenerateFrameBuffers()
    }
}