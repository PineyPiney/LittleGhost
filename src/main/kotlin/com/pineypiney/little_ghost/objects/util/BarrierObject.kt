package com.pineypiney.little_ghost.objects.util

import com.pineypiney.game_engine.objects.Collidable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.GameObject2D
import com.pineypiney.game_engine.objects.util.collision.CollisionBox2D
import com.pineypiney.game_engine.objects.util.collision.HardCollisionBox
import com.pineypiney.game_engine.util.ResourceKey
import glm_.vec2.Vec2

class BarrierObject(position: Vec2, scale: Vec2, rotation: Float = 0f): GameObject2D(), Collidable {

    override val id: ResourceKey = ResourceKey("barrier")

    override val collider: CollisionBox2D = HardCollisionBox(this, Vec2(0), Vec2(1))

    init {
        this.position = position
        this.scale = scale
        this.rotation = rotation
    }

    override fun copy(): BarrierObject {
        return BarrierObject(position, scale, rotation)
    }
}