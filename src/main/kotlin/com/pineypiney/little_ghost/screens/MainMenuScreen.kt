package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.game_objects.objects_2D.SimpleTexturedGameObject2D
import com.pineypiney.game_engine.objects.menu_items.TextButton
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.*
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.audio.AudioType
import glm_.f
import glm_.i
import glm_.vec2.Vec2
import glm_.vec4.Vec4
import kotlin.math.abs
import kotlin.math.sign

class MainMenuScreen(gameEngine: LittleEngine) : MenuScreen("clouds2", gameEngine) {

    private val title = SizedStaticText("Little Ghost", window, Vec2(2, 0.5), Vec4(1))

    private val startButton = TextButton("Start Game", Vec2(-0.4, -0.1), Vec2(0.8, 0.3), window){
        setGame(LittleGameScene(gameEngine))
        openGame()
    }

    private val optionsButton = TextButton("Options", Vec2(-0.4, -0.5), Vec2(0.8, 0.3), window){
        setMenu(OptionsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    private val exitButton = TextButton("Close", Vec2(-0.4, -0.9), Vec2(0.8, 0.3), window){
        window.shouldClose = true
    }

    private var backgroundDirection = 1

    private var ghostDirection = 1
    private val ghost = SimpleTexturedGameObject2D(ResourceKey("ghost"), TextureLoader.getTexture(ResourceKey("characters/ghost")), Shape.centerSquareShape2D)

    private val music = AudioLoader[ResourceKey("lavender")]

    override fun init() {
        super.init()

        title.init()

        ghost.position = Vec2(0, 3)
        ghost.scale = Vec2(2, 2 / ghost.texture.aspectRatio)

        play(Audio(music, AudioType.MUSIC, 1f, 0.6f), true)
    }

    override fun addObjects() {
        super.addObjects()

        add(startButton)
        add(optionsButton)
        add(exitButton)
        add(ghost)
    }

    override fun render(window: Window, tickDelta: Double) {
        super.render(window, tickDelta)

        var amount = backgroundDirection * Timer.frameDelta.f * 0.2f
        if(abs(background.origin.x + amount) > background.size.x - 1) backgroundDirection = -(background.origin.x.sign.i)
        else background.origin.x += amount

        amount = ghostDirection * Timer.frameDelta.f
        ghost.rotation += amount * 2
        ghost.translate(Vec2(amount * 3, 0))
        if(abs(ghost.position.x) > camera.getSpan().x / 2 - 0.5) ghostDirection = -(ghost.position.x.sign.i)
        ghost.render(camera.getView(), camera.getProjection(), tickDelta)

        title.drawCenteredTop(Vec2(0, 0.95))
    }

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)
        title.updateAspectRatio(window)

        background.size.x = background.texture.aspectRatio / window.aspectRatio
    }

    override fun cleanUp() {
        super.cleanUp()
        title.delete()
    }
}