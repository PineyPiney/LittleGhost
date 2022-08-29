package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.objects.menu_items.MenuItem
import com.pineypiney.game_engine.objects.text.Text
import glm_.vec3.Vec3

class TextBackground(val colour: Vec3, val text: Text): MenuItem() {

    override fun init() {
        super.init()
    }

    override fun setUniforms() {
        super.setUniforms()
        uniforms.setVec3Uniform("colour"){colour}
    }

    override fun draw() {
        super.draw()

    }
}