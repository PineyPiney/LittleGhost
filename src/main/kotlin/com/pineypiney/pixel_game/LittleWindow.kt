package com.pineypiney.pixel_game

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.util.input.GamepadInput
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.game_engine.util.input.KeyboardInput
import com.pineypiney.game_engine.util.input.MouseInput

class LittleWindow(title: String, width: Int, height: Int, vSync: Boolean) : Window(title, width, height, vSync) {

    override val input: Inputs = object : Inputs(this){
        override val gamepad: GamepadInput = GamepadInput(this)
        override val keyboard: KeyboardInput = KeyboardInput(this)
        override val mouse: MouseInput = MouseInput(this)
    }

    companion object{
        val INSTANCE = LittleWindow("Little Ghost", 960, 540, false)
    }
}