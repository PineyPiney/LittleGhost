package com.pineypiney.little_ghost.objects

import com.pineypiney.game_engine.objects.game_objects.objects_2D.GameObject2D
import com.pineypiney.game_engine.objects.game_objects.objects_2D.SimpleTexturedGameObject2D
import com.pineypiney.game_engine.objects.game_objects.objects_2D.TexturedGameObject2D
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.objects.decorative.Cloud

class GameObjects {

    companion object{

        val bush1: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Bush1"), ResourceKey("background/foliage/bushes/bush1"))
        val bush2: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Bush2"), ResourceKey("background/foliage/bushes/bush2"))
        val tree1: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Tree1"), ResourceKey("background/foliage/trees/tree1"))
        val tree2: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Tree2"), ResourceKey("background/foliage/trees/tree2"))
        val cloud1: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud1"), ResourceKey("background/clouds/cloud1"))
        val cloud2: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud2"), ResourceKey("background/clouds/cloud2"))
        val cloud3: TexturedGameObject2D; get() = Cloud(ResourceKey("Cloud3"), ResourceKey("background/clouds/cloud3"))
        val mountain1: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Mountain1"), ResourceKey("backgrounds/mountains/mountain1"))
        val mountain2: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Mountain2"), ResourceKey("backgrounds/mountains/mountain2"))
        val mountain3: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Mountain3"), ResourceKey("backgrounds/mountains/mountain3"))
        val volcano: TexturedGameObject2D; get() = SimpleTexturedGameObject2D(ResourceKey("Volcano"), ResourceKey("backgrounds/mountains/volcano"))

        fun getAllItems(): List<GameObject2D> = listOf(
            bush1,
            bush2,
            tree1,
            tree2,
            cloud1,
            cloud2,
            cloud3,
            mountain1,
            mountain2,
            mountain3,
            volcano,
        )

        fun getObject(id: String): GameObject2D? {
            val items = getAllItems()
            return items.firstOrNull { it.id == ResourceKey(id) }
        }
    }
}