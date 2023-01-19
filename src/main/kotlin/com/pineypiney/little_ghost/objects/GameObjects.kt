package com.pineypiney.little_ghost.objects

import com.pineypiney.game_engine.objects.game_objects.objects_2D.GameObject2D
import com.pineypiney.game_engine.objects.game_objects.objects_2D.TexturedGameObject2D
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.objects.decorative.Cloud

class GameObjects {

    companion object{

        val cloud1: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud1"), ResourceKey("background/clouds/cloud1"))
        val cloud2: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud2"), ResourceKey("background/clouds/cloud2"))
        val cloud3: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud3"), ResourceKey("background/clouds/cloud3"))

        fun getAllItems(): List<GameObject2D> = listOf(
            cloud1,
            cloud2,
            cloud3
        )

        fun getObject(id: String): GameObject2D? {
            val items = getAllItems()
            return items.firstOrNull { it.id == ResourceKey(id) }
        }
    }
}