package com.pineypiney.pixel_game.screens

import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.pixel_game.LittleEngine
import com.pineypiney.pixel_game.openMenu
import com.pineypiney.pixel_game.setMenu
import org.lwjgl.glfw.GLFW

abstract class SubMenuScreen(backgroundName: String, val parent: MenuScreen, gameEngine: LittleEngine): MenuScreen(backgroundName, gameEngine) {

    override fun onInput(state: InputState, action: Int): Int {
        if(super.onInput(state, action) == INTERRUPT) return INTERRUPT
        if(state.i == GLFW.GLFW_KEY_ESCAPE && action == 1){
            setMenu(parent)
            openMenu(false)
            return INTERRUPT
        }
        return action
    }

    override fun updateVolume() {
        super.updateVolume()
        parent.updateVolume()
    }

    override fun regenerateFrameBuffers() {
        super.regenerateFrameBuffers()
        parent.regenerateFrameBuffers()
    }
}