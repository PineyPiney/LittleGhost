package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.slider.BasicSliderPointer
import com.pineypiney.game_engine.objects.menu_items.slider.OutlinedSlider
import com.pineypiney.game_engine.objects.menu_items.slider.SliderPointer
import com.pineypiney.little_ghost.LittleWindow
import glm_.vec2.Vec2

class MenuSlider(override var origin: Vec2, override val size: Vec2, override val low: Float, override val high: Float, override var value: Float, val action: (Float) -> Unit): OutlinedSlider() {

    override val window: Window = LittleWindow.INSTANCE
    override val pointer: SliderPointer = BasicSliderPointer(this, 1f)

    override fun moveSliderTo(move: Float) {
        super.moveSliderTo(move)
        action(value)
    }
}