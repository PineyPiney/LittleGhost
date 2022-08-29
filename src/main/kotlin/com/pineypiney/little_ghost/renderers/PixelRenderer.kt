package com.pineypiney.little_ghost.renderers

import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.ObjectCollection
import com.pineypiney.game_engine.rendering.BufferedGameRenderer
import com.pineypiney.game_engine.rendering.FrameBuffer
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.LittleLogic
import com.pineypiney.little_ghost.LittleWindow
import glm_.vec2.Vec2i
import org.lwjgl.opengl.GL46C.*

abstract class PixelRenderer<E: LittleLogic>: BufferedGameRenderer<E>() {

    final override val window: Window = LittleWindow.INSTANCE

    override fun init(){
        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        regenerateFrameBuffers()
    }

    abstract fun regenerateFrameBuffers()

    protected fun renderGUI(game: IGameLogic){
        for(o in game.gameObjects.guiItems.filter { it.visible }){
            o.draw()
        }
    }

    protected fun drawTexture(buffer: FrameBuffer, effects: Int = 0){
        screenShader.use()
        screenShader.setInt("effects", effects)
        drawBufferTexture(buffer)
    }

    protected fun viewport(size: Vec2i) = glViewport(0, 0, size.x, size.y)

    override fun updateAspectRatio(window: Window, objects: ObjectCollection){
        regenerateFrameBuffers()
    }

    companion object{
        val screenShader = ShaderLoader.getShader(ResourceKey("vertex/frame_buffer"), ResourceKey("fragment/frame_buffer"))
    }
}