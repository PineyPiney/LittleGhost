package com.pineypiney.little_ghost.objects.decorative

import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.objects.Updateable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.RenderedGameObject2D
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.angle
import com.pineypiney.game_engine.util.extension_functions.fromAngle
import com.pineypiney.game_engine.util.extension_functions.wrap
import glm_.f
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import kotlin.math.PI
import kotlin.math.sign
import kotlin.random.Random

class FireFly(var target: Vec2, val turnSpeed: Float = 1f, val speed: Float = 1f): BackgroundObject(defaultShader), Updateable {

    var angle = Random.nextFloat() * 6.28f

    override val id: ResourceKey = ResourceKey("FireFly")
    override val texture: Texture = TextureLoader[ResourceKey("background/firefly")]

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        translate(Vec2.fromAngle(angle, speed * Timer.frameDelta.f))
        super.render(view, projection, tickDelta)
    }

    override fun update(interval: Float, time: Double) {
        turn()
    }

    fun turn(){
        val targetAngle = (target - position).angle()
        val turn = (targetAngle - angle).wrap(-PI.f, PI.f)
        angle += turnSpeed * turn.sign * (-0.5f + 1.5f * Random.nextFloat()) * Timer.delta.f
    }

    override fun shouldUpdate(): Boolean {
        return true
    }

    override fun copy(): RenderedGameObject2D {
        return FireFly(target)
    }

}