package com.pineypiney.little_ghost.objects.menu_items.dialogue

import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.util.extension_functions.delete
import com.pineypiney.game_engine.util.extension_functions.init
import com.pineypiney.game_engine.util.maths.shapes.Rect
import com.pineypiney.game_engine.util.raycasting.Ray
import com.pineypiney.little_ghost.util.ScriptProcessor
import glm_.f
import glm_.i
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4
import kotlin.math.floor

class ChoiceDialogue(override val parent: ScriptProcessor, val choiceDepth: Int, val choicePos: Int, choices: List<String>): DialogueBubble() {

    var chosen: Int = 0
    val choices = choices.map { DialogueText(it, parent.game, 75, colour = textColour) }
    val choiceIndices get() = parent.choiceIndices[choiceDepth]

    override val textLoaded: Boolean get() = choices.all { it.finished }

    override fun init() {
        scale = Vec2(choices.maxOf(DialogueText::getGameWidth), choices.sumOf(DialogueText::getGameHeight)) + Vec2(padding * 2)

        choices.init()
        choices.forEachIndexed { i, t -> t.transform.position = position + (scale * Vec2(0.1, (i.f / choices.size))) + Vec2(0, t.getGameHeight()) }

        parent.choiceIndices[choiceDepth] = getChoiceIndexes()

        super.init()
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

        choices.forEach { it.render(view, projection, tickDelta) }
    }

    override fun quickLoadText() {
        choices.forEach(DialogueText::quickLoad)
    }

    override fun updateCursorPos(ray: Ray) {
        super.updateCursorPos(ray)

        chosen = getSelected(Vec2(ray.intersects(Rect(position, scale))!!))
        choices.forEachIndexed { i, t ->
            if(i == chosen) t.colour = selectColour
            else t.colour = textColour
        }
    }

    override fun checkHover(ray: Ray, screenPos: Vec2): Boolean {
        return super.checkHover(ray, screenPos).apply { if(this) updateCursorPos(ray) }
    }

    override fun onPrimary(game: IGameLogic, action: Int, mods: Byte, cursorPos: Vec2): Int {
        val choicePos = choiceIndices?.get(chosen)
        parent.line =  choicePos!!
        return super.onPrimary(game, action, mods, cursorPos)
    }

    fun getChoiceIndexes(): IntArray{
        var i = 0
        var lineI = choicePos
        val choicePoses = mutableListOf<Int>()
        while(true){
            val line = parent.lines[++lineI].trimStart()
            if(line.contains('{') && !line.contains('"')) {
                i++
                if(i == 1){
                    choicePoses.add(lineI)
                }
            }
            else if(line.startsWith('}')){
                i--
                if(i == 0 && choicePoses.size == choices.size) break
            }
        }
        return choicePoses.toIntArray() + lineI
    }

    fun getSelected(worldPos: Vec2) = floor(((worldPos.y - position.y) / scale.y) * choices.size).i.coerceIn(choices.indices)

    override fun update(interval: Float, time: Double) {
        super.update(interval, time)

        choices.forEach { if(it.shouldUpdate()) it.update(interval, time) }
    }

    override fun delete() {
        super.delete()

        choices.delete()
    }

    companion object{
        val selectColour = Vec4(0, 0, 1, 1)
    }
}

fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float{
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}