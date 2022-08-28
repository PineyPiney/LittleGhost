package com.pineypiney.pixel_game.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.TextButton
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.game_engine.resources.video.Video
import com.pineypiney.pixel_game.LittleEngine
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class VideoScreen(gameEngine: LittleEngine, val video: Video) : MenuScreen("Video", gameEngine) {

    private val title = SizedStaticText("Video!", window, Vec2(2, 0.5), Vec4(0, 0, 0 ,1))

    private val exitButton = TextButton("X", Vec2(0.9, 0.9), Vec2(0.08, 0.08), window){
        window.setShouldClose()
    }

    override fun init() {
        super.init()

        add(exitButton)

        exitButton.init()
    }

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)
        title.updateAspectRatio(window)
    }

    override fun render(window: Window, tickDelta: Double) {
        super.render(window, tickDelta)
        title.drawCentered(Vec2(0, 0.6))
    }

    override fun cleanUp() {
        super.cleanUp()
        title.delete()
    }
}