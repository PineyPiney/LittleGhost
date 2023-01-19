package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import org.lwjgl.glfw.GLFW

abstract class SubMenuScreen(val parent: MenuScreen, gameEngine: LittleEngine): MenuScreen(gameEngine) {

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

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)
        parent.updateAspectRatio(window)
    }

    override fun regenerateFrameBuffers() {
        super.regenerateFrameBuffers()
        parent.regenerateFrameBuffers()
    }
}