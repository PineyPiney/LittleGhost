package com.pineypiney.pixel_game.objects.entities.characters

import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.pixel_game.screens.LittleGameScene
import glm_.vec2.Vec2

class BunnyCharacter(scene: LittleGameScene): Character(scene, ResourceKey("Bunny")) {

    override var stillFrames: List<Texture> = listOf(
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\shit official bunny standing"))
    )

    override var movingFrames: List<Texture> = listOf(
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 1")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 2")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 3")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 4")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 5")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 6")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 7")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\walking\\shit official bunny walking 8")),
    )

    override var sprintingFrames: List<Texture> = listOf(
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 1")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 2")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 3")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 4")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 5")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 6")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 7")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 8")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 9")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 10")),
        TextureLoader.getTexture(ResourceKey("characters\\bunny_character\\running\\bunny running 11")),
    )

    override fun init() {
        super.init()

        this.scale = this.scale * Vec2(90, 141) * 0.03f
    }

    override fun shouldUpdate(): Boolean = true

}