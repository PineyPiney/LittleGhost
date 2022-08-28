package com.pineypiney.pixel_game.objects.entities.characters

import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.pixel_game.screens.LittleGameScene
import glm_.vec2.Vec2

open class StillCharacter(scene: LittleGameScene, key: ResourceKey, texture: Texture, width: Float): Character(scene, key) {

    override var stillFrames: List<Texture> = listOf(texture)
    override var movingFrames: List<Texture> = listOf()
    override var sprintingFrames: List<Texture> = listOf()

    init {
        this.scale = Vec2(width, width / texture.aspectRatio)
    }

    override fun calcCurrentFrame(): Texture {
        return stillFrames[0]
    }
}