package com.pineypiney.pixel_game.objects.entities.characters

import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.pixel_game.screens.LittleGameScene
import glm_.vec2.Vec2

class InteractableCharacter(scene: LittleGameScene, key: ResourceKey, texture: Texture, width: Float, val interact: (Int, Vec2) -> Unit): StillCharacter(scene, key, texture, width) {

    override fun onPrimary(game: IGameLogic, action: Int, mods: Byte, cursorPos: Vec2): Int {
        interact(action, cursorPos)
        return super.onPrimary(game, action, mods, cursorPos)
    }
}