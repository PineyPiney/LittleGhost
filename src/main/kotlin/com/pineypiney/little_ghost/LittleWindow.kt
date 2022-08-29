package com.pineypiney.little_ghost

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.util.input.GamepadInput
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.game_engine.util.input.KeyboardInput
import com.pineypiney.game_engine.util.input.MouseInput
import glm_.vec2.Vec2i

class LittleWindow(title: String, width: Int, height: Int, vSync: Boolean) : Window(title, width, height, vSync, Vec2i(4, 0)) {

    override val input: Inputs = object : Inputs(this){
        override val gamepad: GamepadInput = GamepadInput(this)
        override val keyboard: KeyboardInput = KeyboardInput(this)
        override val mouse: MouseInput = MouseInput(this)
    }

    companion object{
        val INSTANCE = LittleWindow("Little Ghost", 960, 540, false)
    }
}