package com.pineypiney.pixel_game.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.Interactable
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.pixel_game.LittleEngine
import com.pineypiney.pixel_game.LittleLogic
import com.pineypiney.pixel_game.objects.menu_items.MenuBackground
import com.pineypiney.pixel_game.renderers.MenuRenderer
import org.lwjgl.glfw.GLFW

open class MenuScreen(val background: MenuBackground, gameEngine: LittleEngine) : LittleLogic(gameEngine) {

    constructor(background: Texture, gameEngine: LittleEngine): this(MenuBackground(background), gameEngine)
    constructor(backgroundName: String, gameEngine: LittleEngine): this(MenuBackground("backgrounds/$backgroundName"), gameEngine)

    final override val renderer: MenuRenderer = MenuRenderer()

    override fun init() {
        super.init()

        update(0f, gameEngine.input)
        updateAspectRatio(window)
    }

    override fun addObjects() {
        add(background)
    }

    override fun open() {
        super.open()

        val ray = camera.getRay(input.mouse.lastPos)
        for(item in gameObjects.guiItems.filterIsInstance<Interactable>()){
            item.pressed = false
            item.hover = item.checkHover(ray, input.mouse.lastPos)
        }

        // Add mouse
        GLFW.glfwSetInputMode(window.windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
    }

    override fun render(window: Window, tickDelta: Double) {
        renderer.render(window, this, tickDelta)
    }

    override fun cleanUp() {
        super.cleanUp()

        background.delete()
    }
}