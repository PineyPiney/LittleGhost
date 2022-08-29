package com.pineypiney.little_ghost

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.input.GamepadInput
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.game_engine.util.input.KeyboardInput
import com.pineypiney.game_engine.util.input.MouseInput
import glm_.vec2.Vec2i
import kool.lib.toByteArray
import kool.toBuffer
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage

class LittleWindow(title: String, width: Int, height: Int, vSync: Boolean) : Window(title, width, height, vSync) {

    override val input: Inputs = object : Inputs(this){
        override val gamepad: GamepadInput = GamepadInput(this)
        override val keyboard: KeyboardInput = KeyboardInput(this)
        override val mouse: MouseInput = MouseInput(this)
    }

    fun setCursor(texture: Texture, point: Vec2i){
        // The data must be in RGBA 32-bit format
        val data = texture.getData().toByteArray().toList()
        val pixels = data.chunked(texture.numChannels)
        val flipped = pixels.chunked(texture.width).reversed().flatten()
        val rgba = flipped.flatMap { p -> List(4){ p.getOrElse(it){-1} } }

        val image = GLFWImage.create().set(texture.width, texture.height, rgba.toByteArray().toBuffer())
        val handle = GLFW.glfwCreateCursor(image, point.x, point.y)
        GLFW.glfwSetCursor(windowHandle, handle)
    }

    companion object{
        val INSTANCE = LittleWindow("Little Ghost", 960, 540, false)
    }
}