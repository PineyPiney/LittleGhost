package com.pineypiney.little_ghost.objects.menu_items.dialogue

import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.objects.game_objects.objects_2D.InteractableGameObject2D
import com.pineypiney.game_engine.objects.game_objects.objects_2D.RenderedGameObject2D
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.maths.shapes.Rect3D
import com.pineypiney.game_engine.util.raycasting.Ray
import com.pineypiney.little_ghost.util.ScriptProcessor
import glm_.f
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4

abstract class DialogueBubble: InteractableGameObject2D(shader) {

    override val id: ResourceKey = ResourceKey("Dialogue")

    abstract val textLoaded: Boolean
    abstract val parent: ScriptProcessor

    var startTime = 0f
    val opaqueness: Float get() = Timer.frameTime.f - startTime
    val padding = .5f

    override fun init() {
        super.init()
        val ray = parent.game.camera.getRay()
        updateCursorPos(ray)
    }

    override fun setUniforms() {
        super.setUniforms()
        uniforms.setVec4Uniform("colour"){ Vec4(1, 1, 1, opaqueness) }
        uniforms.setFloatUniform("radius"){ 0.2f }
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

        Shape.cornerSquareShape2D.run {
            bind()
            draw()
        }
    }

    override fun onPrimary(game: IGameLogic, action: Int, mods: Byte, cursorPos: Vec2): Int {
        if(action == 1) {
            if(textLoaded) parent.update()
            else quickLoadText()
        }
        return super.onPrimary(game, action, mods, cursorPos)
    }

    abstract fun quickLoadText()

    open fun updateCursorPos(ray: Ray){
        hover = ray.passesThroughRect(Rect3D(position, scale))
    }

    override fun copy(): RenderedGameObject2D {
        TODO("Not yet implemented")
    }

    companion object{
        val textColour = Vec4(107, 86, 100, 255) / 255
        val shader = ShaderLoader.getShader(ResourceKey("vertex/2D"), ResourceKey("fragment/dialogue_bubble"))
    }
}