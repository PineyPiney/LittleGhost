package com.pineypiney.little_ghost.objects.entities.characters

import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.screens.LittleGameScene
import glm_.vec2.Vec2

open class StillCharacter(scene: LittleGameScene, key: ResourceKey, texture: Texture, width: Float): Character(scene, key) {

    override var stillFrames: List<Texture> = listOf(texture)
    override var movingFrames: List<Texture> = listOf()
    override var sprintingFrames: List<Texture> = listOf()

    init {
        scale = Vec2(width, width / texture.aspectRatio)
    }

    override fun calcCurrentFrame(): Texture {
        return stillFrames[0]
    }

    override fun copy(): StillCharacter {
        return StillCharacter(scene, id, stillFrames[0], scale.x)
    }
}