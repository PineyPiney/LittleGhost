package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import com.pineypiney.little_ghost.openGame
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW

class PauseMenuScreen(gameEngine: LittleEngine) : MenuScreen(gameEngine) {

    private val resumeButton = TextButton("Resume", Vec2(-0.4, 0.2), 0.4f, 0xF5E5DF, 0.8f){
        openGame(false)
    }

    private val optionsButton = TextButton("Options", Vec2(-0.4, -0.5), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(OptionsMenuScreen(this, gameEngine), false)
        openMenu()
    }

    private val exitButton = TextButton("Exit", Vec2(-0.4, -0.9), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(MainMenuScreen(gameEngine))
        openMenu()
    }

    override fun addObjects() {
        super.addObjects()

        add(resumeButton)
        add(optionsButton)
        add(exitButton)
    }

    override fun onInput(state: InputState, action: Int): Int {
        if(super.onInput(state, action) == INTERRUPT) return INTERRUPT

        if(action == GLFW.GLFW_RELEASE) {
            if (state.i == GLFW.GLFW_KEY_ESCAPE) {
                openGame(false)
                return INTERRUPT
            }
        }

        return action
    }
}