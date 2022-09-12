package com.pineypiney.little_ghost.objects.decorative

import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import glm_.f
import glm_.vec3.Vec3
import kotlin.random.Random

class Lamppost: BackgroundObject(lamppostShader) {

    override val id: ResourceKey = ResourceKey("Lamppost")
    override val texture: Texture = TextureLoader[ResourceKey("background/lamppost")]

    override fun setUniforms() {
        super.setUniforms()
        uniforms.setVec3Uniform("bright"){ Vec3(255, 206, 33) / 255 }
        uniforms.setVec3Uniform("dim"){ Vec3(255, 90, 20) / 255 }
        uniforms.setFloatUniform("brightness"){ Random.nextDouble(0.7, 1.0).f }
    }

    override fun copy(): Lamppost {
        return Lamppost()
    }

    companion object {
        val lamppostShader: Shader = ShaderLoader[ResourceKey("vertex/2D"), ResourceKey("fragment/lamppost")]
    }
}