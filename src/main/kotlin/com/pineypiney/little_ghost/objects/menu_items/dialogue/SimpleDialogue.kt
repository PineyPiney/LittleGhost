package com.pineypiney.little_ghost.objects.menu_items.dialogue

import com.pineypiney.little_ghost.util.ScriptProcessor
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class SimpleDialogue(override val parent: ScriptProcessor, name: String, speech: String): DialogueBubble() {

    val nameText = DialogueText(name, parent.game, 75, Vec2((parent.game.camera.getSpan().x / 2) - padding * 2), textColour)
    val speechText = DialogueText(speech.split("\\n").joinToString("\n") { it.trim() }, parent.game, 75, Vec2((parent.game.camera.getSpan().x / 2) - padding * 2), textColour)
    val nameBack = NameBackground(nameText, this)

    override val textLoaded: Boolean get() = speechText.finished

    override fun init() {

        nameText.init()
        speechText.init()

        scale = Vec2(speechText.getGameWidth(), speechText.getGameHeight()) + Vec2(padding * 2)

        nameText.let {
            it.transform.position = position + (scale * Vec2(0.75f, 1f) - it.getGameSize()/2)
        }
        speechText.let {
            it.transform.position = position + Vec2(0, (it.lines.size - 1) * it.defaultCharHeight) + Vec2(padding)
        }

        nameBack.init()

        nameText.quickLoad()

        super.init()
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

        nameText.colour.w = opaqueness
        speechText.colour.w = opaqueness

        nameBack.render(view, projection, tickDelta)
        nameText.render(view, projection, tickDelta)
        speechText.render(view, projection, tickDelta)
    }

    override fun quickLoadText() {
        speechText.quickLoad()
    }

    override fun update(interval: Float, time: Double) {
        super.update(interval, time)

        if(speechText.shouldUpdate()) speechText.update(interval, time)
    }

    override fun delete() {
        super.delete()

        nameText.delete()
        speechText.delete()
    }

    companion object{
        val nameBackgroundColour = Vec4(183, 164, 160, 255) / 255
    }
}