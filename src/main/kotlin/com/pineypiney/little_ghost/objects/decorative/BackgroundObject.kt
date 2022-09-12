package com.pineypiney.little_ghost.objects.decorative

import com.pineypiney.game_engine.objects.game_objects.objects_2D.TexturedGameObject2D
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.Shader
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2

abstract class BackgroundObject(shader: Shader): TexturedGameObject2D(shader) {

    open val shape = Shape.footSquare

    var width: Float
    get() = scale.x
    set(value) { scale = Vec2(value, value / texture.aspectRatio) }

    override fun init() {
        super.init()
        width = scale.x
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

        texture.bind()

        shape.bind()
        shape.draw()
    }
}