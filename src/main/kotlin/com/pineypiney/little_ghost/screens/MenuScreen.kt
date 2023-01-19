package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.Interactable
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.LittleLogic
import com.pineypiney.little_ghost.renderers.MenuRenderer
import glm_.f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11C

open class MenuScreen(gameEngine: LittleEngine) : LittleLogic(gameEngine) {

    final override val renderer: MenuRenderer = MenuRenderer()

    override fun init() {
        super.init()

        update(0f, gameEngine.input)
        updateAspectRatio(window)
    }

    override fun open() {
        super.open()
        setColour(0xD4A290)

        val ray = camera.getRay()
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

    fun setColour(r: Int, g: Int, b: Int, a: Int = 255){
        GL11C.glClearColor(r.f/255, g.f/255, b.f/255, a.f/255)
    }

    fun setColour(colour: Int, a: Int = 255){
        setColour((colour shr 16) and 255, (colour shr 8) and 255, colour and 255, a)
    }

    override fun cleanUp() {
        super.cleanUp()
    }

    override fun addObjects() {
    }
}