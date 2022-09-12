package com.pineypiney.little_ghost.objects.decorative

import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.objects.util.shapes.SquareShape
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.ResourceKey

class Background(override val texture: Texture): BackgroundObject(defaultShader) {

    override val id: ResourceKey = ResourceKey("")

    override val shape: SquareShape = Shape.centerSquareShape2D

    override fun init() {
        super.init()
        width = 10 * texture.aspectRatio
    }

    override fun copy(): Background {
        return Background(texture)
    }
}