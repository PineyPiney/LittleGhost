package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.objects.menu_items.AbstractButton
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.maths.I
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.audio.AudioType
import com.pineypiney.little_ghost.gameEngine
import glm_.func.common.clamp
import glm_.vec2.Vec2
import glm_.vec3.Vec3

class MenuButton(texture: Texture, origin: Vec2, height: Float, action: (AbstractButton) -> Unit): ImageButton(texture, origin, height, action) {

    private val underlineModel get() = I.translate(Vec3(origin - Vec2(0, height / 3))).scale(Vec3(size * Vec2(1, 0.3f)))
    private var underlineV = 0f
        set(value){
            field = value.clamp(0f, 1f)
        }

    override fun draw() {
        super.draw()

        if(underlineV > 0){
            underlineShader.use()
            underlineShader.setMat4("model", underlineModel)
            underlineShader.setFloat("amount", underlineV)
            underline.bind()
            shape.draw()
        }
    }

    override fun update(interval: Float, time: Double) {
        super.update(interval, time)

        if(hover && underlineV == 0f) gameEngine.activeScreen.play(Audio(scribble, AudioType.SFX, 0.3f))

        if(hover && underlineV < 1f) underlineV += interval * 3f
        else if(!hover && underlineV > 0f) underlineV -= interval * 3f
    }

    companion object{
        val underline = TextureLoader[ResourceKey("menus/underline")]
        val underlineShader = ShaderLoader[ResourceKey("vertex/menu"), ResourceKey("fragment/underline")]

        private val scribble = AudioLoader[ResourceKey("scribble")]
    }
}