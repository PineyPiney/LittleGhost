package com.pineypiney.pixel_game.objects.menu_items.dialogue

import com.pineypiney.game_engine.objects.Updateable
import com.pineypiney.game_engine.objects.text.SizedGameText
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.pixel_game.LittleLogic
import com.pineypiney.pixel_game.audio.Audio
import com.pineypiney.pixel_game.audio.AudioType
import glm_.c
import glm_.f
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4

class DialogueText(text: String, val game: LittleLogic, size: Int, bounds: Vec2 = Vec2(2), colour: Vec4 = Vec4(1)): SizedGameText(text, size, bounds, colour), Updateable {

    var timeTillNextCharacter = 0f
    var shownCharacters = 0
        set(value) {
            field = value.coerceIn(0, letterIndices.size)
        }

    val finished: Boolean get() = shownCharacters == letterIndices.size

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        shader.use()
        shader.setUniforms(uniforms)
        shader.setMat4("view", view)
        shader.setMat4("projection", projection)

        font.texture.bind()

        val originModel = transform.model

        var yOffset = separation * (lines.size - 1)
        var i = 0
        for(line in lines){

            // Add a bit of space at the beginning
            var xOffset = font.characterSpacing.f

            for(j in line.indices){
                if(i >= shownCharacters){
                    return
                }

                setIndividualUniforms(shader, i)

                val charWidth = getCharWidth(line[j]).f
                quads[i].bind()

                var model = originModel.scale(Vec3(defaultCharWidth * (charWidth/font.letterWidth), defaultCharHeight, 1))
                model = model.translate(Vec3(xOffset  / charWidth, yOffset, 0))
                shader.setMat4("model", model)

                quads[i].draw()
                xOffset += (charWidth + font.characterSpacing)
                i++
            }

            yOffset -= 0.6f
        }
    }

    fun getGameWidth(): Float{
        if(lines.isEmpty()) return 0f
        return lines.maxOf(::getLineWidth)
    }

    fun getLineWidth(line: String): Float{
        return (defaultCharWidth / font.letterWidth) * getPixelWidth(line)
    }

    fun getGameHeight(): Float{
        return separation * lines.size * defaultCharHeight
    }

    fun getGameSize(): Vec2 = Vec2(getGameWidth(), getGameHeight())

    fun quickLoad(){
        shownCharacters = letterIndices.size
    }

    fun addCharacter(){
        timeTillNextCharacter += timeForChar((letterIndices[shownCharacters] + 32).c)
        shownCharacters++

        game.play(Audio(AudioLoader[ResourceKey("text/type_${soundForTime(timeTillNextCharacter)}")], AudioType.SFX))
    }

    override fun update(interval: Float, time: Double) {
        timeTillNextCharacter -= interval
        if(timeTillNextCharacter <= 0){
            addCharacter()
        }
    }

    override fun shouldUpdate(): Boolean {
        return !finished
    }

    fun timeForChar(char: Char): Float{
        return when(char){
            '.', '?', '!' -> 0.7f
            ',', '-' -> 0.3f
            else -> 0.06f
        }
    }

    fun soundForTime(time: Float): Int{
        return when{
            time >= 0.2f -> 3
            else -> (1..2).random()
        }
    }
}