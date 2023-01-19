package com.pineypiney.little_ghost.objects.menu_items.dialogue

import com.pineypiney.game_engine.objects.game_objects.objects_2D.RenderedGameObject2D
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.fromHex
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class NameBackground(private val text: DialogueText, val parent: DialogueBubble): RenderedGameObject2D(shader){

    override val id: ResourceKey = ResourceKey("Background")
    val border = 0.05f

    override fun init() {
        super.init()
        position = text.transform.position + (text.getGameSize() / 2)
        scale = text.getGameSize() + Vec2(border * 2)
    }

    override fun setUniforms() {
        super.setUniforms()
        uniforms.setVec4Uniform("colour"){ Vec4.fromHex(0xF5E5DF, parent.opaqueness) }
        uniforms.setFloatUniform("radius"){ 0.15f }
        uniforms.setVec4Uniform("borderColour"){ Vec4.fromHex(0xD4A290, parent.opaqueness) }
        uniforms.setFloatUniform("borderSize"){ border }
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)
        Shape.centerSquareShape2D.bind()
        Shape.centerSquareShape2D.draw()
    }

    override fun copy(): NameBackground {
        return NameBackground(text, parent)
    }

    companion object{
        val shader = ShaderLoader[ResourceKey("vertex/2D"), ResourceKey("fragment/bordered_squircle")]
    }
}