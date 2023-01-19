package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.objects.menu_items.scroll_lists.ScrollingListEntry
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.text.Font
import com.pineypiney.little_ghost.window
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class SizedScrollerText(text: String, fontSize: Number = 100, private val limits: Vec2, colour: Vec4 = Vec4(1, 1, 1, 1),
                        maxWidth: Float = 2f, maxHeight: Float = 2f,
                        separation: Float = 0.6f, font: Font = Font.defaultFont,
                        shader: Shader = ScrollingListEntry.entryTextShader): SizedStaticText(text, window, fontSize, colour, maxWidth, maxHeight, separation, font, shader) {

    override fun setUniforms() {
        super.setUniforms()
        // Limit is in 0 to Window#height space so must be transformed
        uniforms.setVec2Uniform("limits"){ (limits + Vec2(1)) * (window.height / 2f) }
    }

    override fun drawUnderline(model: Mat4) {
        val pos = origin.y + defaultCharHeight * underlineThickness * underlineOffset
        if(pos > limits.x && pos < limits.y){
            super.drawUnderline(model)
        }
    }
}