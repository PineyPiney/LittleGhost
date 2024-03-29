package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.KeyBindList
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW

class KeyBindsMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen(parent, gameEngine) {

    private val keys = KeyBindList(Vec2(-0.8, -0.5), Vec2(1.6, 1.3), 0.5f, 0.05f)

    private val backButton = TextButton("Back", Vec2(-0.4, -0.9), 0.3f, 0xF5E5DF, 0.8f) {
        setMenu(this.parent)
        openMenu(false)
    }

    override fun addObjects() {
        super.addObjects()

        add(keys)
        add(backButton)
    }

    override fun onInput(state: InputState, action: Int): Int {
        if(state.i == GLFW.GLFW_KEY_ESCAPE){
            if(action == 1) {
                if(keys.selectedEntry >= 0){
                    keys.getSelectedEntry()?.unselect()
                    keys.selectedEntry = -1
                    return INTERRUPT
                }
            }
        }
        else{
            if(keys.selectedEntry > -1 && action == 0){
                keys.setKeyBind(state)
                return INTERRUPT
            }
        }
        return super.onInput(state, action)
    }
}