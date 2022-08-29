package com.pineypiney.little_ghost.objects.util

import com.pineypiney.game_engine.objects.Collidable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.GameObject2D
import com.pineypiney.game_engine.objects.util.collision.CollisionBox2D
import com.pineypiney.game_engine.objects.util.collision.HardCollisionBox
import com.pineypiney.game_engine.util.ResourceKey

class BarrierObject: GameObject2D(), Collidable {

    override val id: ResourceKey = ResourceKey("barrier")

    override val collider: CollisionBox2D = HardCollisionBox(this, position, scale)

    override fun copy(): BarrierObject {
        return BarrierObject()
    }
}