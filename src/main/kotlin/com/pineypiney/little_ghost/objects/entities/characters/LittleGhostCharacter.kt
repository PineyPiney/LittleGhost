package com.pineypiney.little_ghost.objects.entities.characters

import com.pineypiney.game_engine.objects.util.collision.SoftCollisionBox
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.screens.LittleGameScene
import glm_.vec2.Vec2
import kotlin.math.sign

class LittleGhostCharacter(scene: LittleGameScene, width: Float): Character(scene, ResourceKey("little_ghost"), width) {

    override val collider: SoftCollisionBox = SoftCollisionBox(this, Vec2(-0.075, -0.282), Vec2(0.225, 0.515))

    override var stillFrames: List<Texture> = listOf(TextureLoader[ResourceKey("characters/ben/ghost/ben_sprite")])
    override var movingFrames: List<Texture> = (1..7).map { TextureLoader[ResourceKey("characters/ben/ghost/walking/benwalking$it")] }
    override var sprintingFrames: List<Texture> = movingFrames

    override fun copy(): LittleGhostCharacter {
        return LittleGhostCharacter(scene, scale.x)
    }
}