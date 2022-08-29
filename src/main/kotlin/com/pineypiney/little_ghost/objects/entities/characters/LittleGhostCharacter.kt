package com.pineypiney.little_ghost.objects.entities.characters

import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.screens.LittleGameScene

open class LittleGhostCharacter(scene: LittleGameScene, width: Float): Character(scene, ResourceKey("little_ghost"), width) {

    override var stillFrames: List<Texture> = listOf(TextureLoader[ResourceKey("characters/ben/ghost/talking/bentalking1")])
    override var movingFrames: List<Texture> = (1..7).map { TextureLoader[ResourceKey("characters/ben/ghost/walking/benwalking$it")] }
    override var sprintingFrames: List<Texture> = stillFrames

    override fun copy(): LittleGhostCharacter {
        return LittleGhostCharacter(scene, scale.x)
    }

    override fun calcCurrentFrame(): Texture {
        scale.x = if(walking) 5.6f else 1.4f
        return super.calcCurrentFrame()
    }
}