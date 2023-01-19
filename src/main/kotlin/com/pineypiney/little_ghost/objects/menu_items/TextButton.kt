package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.AbstractButton
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.game_engine.util.extension_functions.fromHex
import com.pineypiney.game_engine.util.maths.I
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.audio.AudioType
import com.pineypiney.little_ghost.gameEngine
import com.pineypiney.little_ghost.window
import glm_.func.common.clamp
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4

open class TextButton(val string: String, override val origin: Vec2, height: Float,
                      textColour: Vec4, override val action: (AbstractButton) -> Unit): AbstractButton() {

    constructor(string: String, origin: Vec2, height: Float, textColour: Int, alpha: Float = 1f, action: (AbstractButton) -> Unit):
            this(string, origin, height, Vec4.fromHex(textColour, alpha), action)

    private val text: SizedStaticText = SizedStaticText(string, window, height * 100, textColour)
    override var size: Vec2 = super.size

    private val underlineModel get() = I.translate(Vec3(origin - Vec2(0, size.y / 3))).scale(Vec3(size * Vec2(1, 0.3f)))
    private var underlineV = 0f
        set(value){
            field = value.clamp(0f, 1f)
        }

    override fun init() {
        text.init()
    }

    override fun draw() {
        text.drawCentered(origin + size * 0.5f)

        if(underlineV > 0){
            MenuButton.underlineShader.use()
            MenuButton.underlineShader.setMat4("model", underlineModel)
            MenuButton.underlineShader.setFloat("amount", underlineV)
            MenuButton.underline.bind()
            shape.bind()
            shape.draw()
        }
    }

    override fun update(interval: Float, time: Double) {
        super.update(interval, time)

        if(hover && underlineV == 0f) gameEngine.activeScreen.play(Audio(MenuButton.scribble, AudioType.SFX, 0.3f))

        if(hover && underlineV < 1f) underlineV += interval * 3f
        else if(!hover && underlineV > 0f) underlineV -= interval * 3f
    }

    override fun updateAspectRatio(window: Window) {
        text.updateAspectRatio(window)
        size = text.getScreenSize()
    }

    override fun delete() {
        super.delete()
        text.delete()
    }
}