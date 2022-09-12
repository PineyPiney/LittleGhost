package com.pineypiney.little_ghost.objects.menu_items.dialogue

import com.pineypiney.game_engine.objects.Updateable
import com.pineypiney.game_engine.objects.text.SizedGameText
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.LittleLogic
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.audio.AudioType
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4

class DialogueText(text: String, val game: LittleLogic, size: Int, bounds: Vec2 = Vec2(16, 9), colour: Vec4 = Vec4(1)): SizedGameText(text, size, bounds, colour), Updateable {

    var timeTillNextCharacter = 0f
    var shownCharacters = 0
        set(value) {
            field = value.coerceIn(0, text.length)
        }

    val finished: Boolean get() = shownCharacters == text.length

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        if(lines.isEmpty()) return

        val originModel = transform.model
        val totalWidth = lines.maxOf { getWidth(it.trim()) }

        var i = 0
        for(line in lines){
            shader.use()
            shader.setUniforms(uniforms)
            shader.setMat4("view", view)
            shader.setMat4("projection", projection)

            val displayLine = line.trim()
            val alignmentOffset = getAlignment(displayLine, totalWidth)
            val lineModel = originModel.translate(alignmentOffset, 0f, 0f).scale(defaultCharHeight, defaultCharHeight, 1f)

            val firstIndex = i + line.indexOfFirst { it != ' ' }
            for(j in displayLine.indices){
                if(firstIndex + j >= shownCharacters){
                    return
                }
                val quad = quads[firstIndex + j]
                setIndividualUniforms(shader, quad)

                quad.bind()

                val model = lineModel.translate(Vec3(quad.offset, 0))
                shader.setMat4("model", model)

                quad.draw()
            }

            if(underlineThickness > 0){
                renderUnderline(lineModel.translate(Vec3(quads[firstIndex].offset, 0)).scale(getWidth(displayLine) / defaultCharHeight, underlineThickness, 0f).translate(0f, underlineOffset, 0f), view, projection)
            }

            i += line.length
        }
    }

    fun getGameWidth(): Float{
        if(lines.isEmpty()) return 0f
        return lines.maxOf { getWidth(it) }
    }

    fun getGameHeight(): Float{
        return lines.size * defaultCharHeight
    }

    fun getGameSize(): Vec2 = Vec2(getGameWidth(), getGameHeight())

    fun quickLoad(){
        shownCharacters = text.length
    }

    fun addCharacter(){
        timeTillNextCharacter += timeForChar(text[shownCharacters])
        shownCharacters++
        // Don't process escape characters
        while( shownCharacters < text.length && text[shownCharacters] == '\n') shownCharacters++

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