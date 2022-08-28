package com.pineypiney.pixel_game.screens

import com.pineypiney.game_engine.objects.menu_items.TextButton
import com.pineypiney.pixel_game.LittleEngine
import com.pineypiney.pixel_game.openMenu
import com.pineypiney.pixel_game.setMenu
import com.pineypiney.pixel_game.util.UserSettings
import glm_.vec2.Vec2
import glm_.vec2.Vec2i

class GraphicsMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen("graphics menu.png", parent, gameEngine) {

    val windowRes = TextButton("Window Resolution", Vec2(-0.4, 0.85), Vec2(0.8, 0.1), window){
        UserSettings.matchWindow = true
        updateBuffers()
    }

    val FHDRes = TextButton("1920 x 1080", Vec2(-0.4, 0.7), Vec2(0.8, 0.1), window){
        setResolution(Vec2i(1920, 1080))
    }

    val HDRes = TextButton("1280 x 720", Vec2(-0.4, 0.55), Vec2(0.8, 0.1), window){
        setResolution(Vec2i(1280, 720))
    }

    val four80 = TextButton("640 x 480", Vec2(-0.4, 0.4), Vec2(0.8, 0.1), window){
        setResolution(Vec2i(640, 480))
    }

    val two40 = TextButton("320 x 240", Vec2(-0.4, 0.25), Vec2(0.8, 0.1), window){
        setResolution(Vec2i(320, 240))
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), Vec2(0.8, 0.3), window){
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