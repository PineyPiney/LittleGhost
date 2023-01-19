package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.game_objects.objects_2D.SimpleTexturedGameObject2D
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.fromHex
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.little_ghost.*
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.audio.AudioType
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class MainMenuScreen(gameEngine: LittleEngine) : MenuScreen(gameEngine) {

    private val startButton = TextButton("Play", Vec2(0, 0.75), 0.1f, Vec4.fromHex(0xF5E5DF, 0.8f)){
        setGame(LittleGameScene(gameEngine))
        openGame()
    }

    private val optionsButton = TextButton("Options", Vec2(-0.15, 0.6), 0.1f, Vec4.fromHex(0xF5E5DF, 0.8f)){
        setMenu(OptionsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    private val exitButton = TextButton("Quit", Vec2(0, 0.45), 0.1f, Vec4.fromHex(0xF5E5DF, 0.8f)){
        window.shouldClose = true
    }

    private val ghost = SimpleTexturedGameObject2D(ResourceKey("ghost"), TextureLoader.getTexture(ResourceKey("characters/ghost")), Shape.centerSquareShape2D)

    private val music = AudioLoader[ResourceKey("lavender")]

    private var target = Vec2()
    private var ben = SimpleTexturedGameObject2D(ResourceKey("ben"), TextureLoader[ResourceKey("menus/ben")], Shape.centerSquareShape2D)

    override fun init() {
        super.init()

        ben.scale = Vec2(6 * ben.texture.aspectRatio, 6)

        play(Audio(music, AudioType.MUSIC, 1f, 0.6f), true)
    }

    override fun addObjects() {
        super.addObjects()

        add(startButton)
        add(optionsButton)
        add(exitButton)
        add(ghost)
        add(ben)
    }

    override fun render(window: Window, tickDelta: Double) {
        super.render(window, tickDelta)

        ben.render(camera.getView(), camera.getProjection(), tickDelta)
    }

    override fun update(interval: Float, input: Inputs) {
        super.update(interval, input)
        val dist = target - ben.position
        if(dist.length2() < 0.001) return

        ben.translate(dist * interval * dist.length())
    }

    override fun onCursorMove(cursorPos: Vec2, cursorDelta: Vec2) {
        super.onCursorMove(cursorPos, cursorDelta)

        target = (-cursorPos.normalize() * 1f)
    }
}