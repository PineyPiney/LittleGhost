package com.pineypiney.pixel_game.objects.menu_items

import com.pineypiney.game_engine.objects.menu_items.MenuItem
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import glm_.vec2.Vec2

class MenuBackground(val texture: Texture): MenuItem() {

    constructor(name: String): this(TextureLoader.getTexture(ResourceKey(name)))

    override val shape: Shape = Shape.screenQuadShape
    override val size: Vec2 = Vec2(1)

    override var shader: Shader = opaqueTextureShader

    override fun draw() {
        texture.bind()
        super.draw()
    }
}