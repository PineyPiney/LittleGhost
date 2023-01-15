package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.AbstractButton
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.textures.Texture
import glm_.vec2.Vec2

open class ImageButton(val texture: Texture, override val origin: Vec2, val height: Float,
                  override val action: (AbstractButton) -> Unit): AbstractButton() {

    override var shader: Shader = opaqueTextureShader
    override var size: Vec2 = super.size

    override fun draw() {
        texture.bind()
        super.draw()
    }

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)
        size = Vec2(height * texture.aspectRatio / window.aspectRatio, height)
    }
}