package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.MenuItem
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.maths.I
import com.pineypiney.game_engine.util.maths.normal
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3

class RatioItem(val texture: Texture, override var origin: Vec2, width: Float): MenuItem() {

    var rotation = 0f

    override val model: Mat4; get() = I.translate(Vec3(origin)).rotate(rotation, normal).scale(Vec3(size))

    override val size: Vec2 = Vec2(width, width / texture.aspectRatio)
    override val shape: Shape = Shape.centerSquareShape2D
    override var shader: Shader = opaqueTextureShader

    override fun draw() {
        texture.bind()
        super.draw()
    }

    override fun updateAspectRatio(window: Window) {
        size.y = size.x * window.aspectRatio / texture.aspectRatio
    }
}