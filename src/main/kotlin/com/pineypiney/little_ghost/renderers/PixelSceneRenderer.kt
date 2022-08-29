package com.pineypiney.little_ghost.renderers

import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.game_objects.objects_2D.RenderedGameObject2D
import com.pineypiney.game_engine.rendering.FrameBuffer
import com.pineypiney.game_engine.util.maths.I
import com.pineypiney.little_ghost.LittleLogic
import com.pineypiney.little_ghost.util.UserSettings
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2i

open class PixelSceneRenderer: PixelRenderer<LittleLogic>() {

    val gameBuffer = FrameBuffer(Vec2i())

    var view: Mat4 = I
    var projection: Mat4 = I

    override fun regenerateFrameBuffers(){
        val res = UserSettings.resolution
        buffer.setSize(res)
        gameBuffer.setSize(res)
    }

    override fun render(window: Window, game: LittleLogic, tickDelta: Double){

        view = game.camera.getView()
        projection = game.camera.getProjection()

        // First, clear the game texture and set the resolution
        viewport(UserSettings.resolution)
        clearFrameBuffer(gameBuffer)

        // And draw on the game
        renderItems(view, projection, tickDelta, game)



        // Then clear the screen texture
        clearFrameBuffer(buffer)
        // Draw on the game screen
        drawTexture(gameBuffer)
        // And draw the HUD
        renderGUI(game)


        // Finally, clear the main screen
        viewport(window.size)
        FrameBuffer.unbind()
        clear()
        // And render the whole screen onto a screen quad
        drawTexture(buffer)
    }

    private fun renderItems(view: Mat4, projection: Mat4, tickDelta: Double, game: IGameLogic){

        val items: List<RenderedGameObject2D> = game.gameObjects.gameItems.filterIsInstance<RenderedGameObject2D>().filter { it.visible }.sortedByDescending { it.depth }
        for(item in items){
            item.render(view, projection, tickDelta)
        }
    }

    override fun deleteFrameBuffers(){
        super.deleteFrameBuffers()
        gameBuffer.delete()
    }
}