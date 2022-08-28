package com.pineypiney.pixel_game.objects.menu_items.dialogue

import com.pineypiney.pixel_game.util.ScriptProcessor
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2

class SimpleDialogue(override val parent: ScriptProcessor, name: String, speech: String): DialogueBubble() {

    val nameText = DialogueText(name, parent.game, 75, Vec2((parent.game.camera.getSpan().x / 2) - padding * 2))
    val speechText = DialogueText(speech.split("\\n").map { it.trim() }.joinToString("\n"), parent.game, 75, Vec2((parent.game.camera.getSpan().x / 2) - padding * 2))

    override val textLoaded: Boolean get() = speechText.finished

    override fun init() {
        super.init()

        nameText.init()
        speechText.init()

        scale = Vec2(speechText.getGameWidth(), speechText.getGameHeight()) + Vec2(padding * 2)

        nameText.let {
            it.transform.position = position + (scale * Vec2(0.75f, 1f) - it.getGameSize()/2)
        }
        speechText.let {
            it.transform.position = position - Vec2(0, it.separation / 2) + Vec2(padding)
        }

        nameText.quickLoad()
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

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
}