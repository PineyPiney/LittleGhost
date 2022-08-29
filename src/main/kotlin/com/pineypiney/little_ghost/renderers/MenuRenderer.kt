package com.pineypiney.little_ghost.renderers

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.rendering.FrameBuffer
import com.pineypiney.little_ghost.screens.MenuScreen
import com.pineypiney.little_ghost.util.UserSettings
import org.lwjgl.opengl.GL11.glViewport

open class MenuRenderer: PixelRenderer<MenuScreen>() {

    override fun regenerateFrameBuffers(){
        val res = UserSettings.resolution
        buffer.setSize(res)
    }

    override fun render(window: Window, game: MenuScreen, tickDelta: Double){

        viewport(UserSettings.resolution)
        // First clear the screen texture
        clearFrameBuffer(buffer)
        // And draw the HUD
        renderGUI(game)


        // Then clear the main screen
        glViewport(0, 0, window.width, window.height)
        FrameBuffer.unbind()
        clear()
        // And render the whole screen onto a screen quad
        drawTexture(buffer)
    }
}