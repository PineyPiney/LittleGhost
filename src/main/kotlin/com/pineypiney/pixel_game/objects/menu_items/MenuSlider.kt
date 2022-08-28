package com.pineypiney.pixel_game.objects.menu_items

import com.pineypiney.game_engine.objects.menu_items.slider.BasicSliderPointer
import com.pineypiney.game_engine.objects.menu_items.slider.OutlinedSlider
import com.pineypiney.game_engine.objects.menu_items.slider.SliderPointer
import com.pineypiney.pixel_game.LittleWindow
import glm_.vec2.Vec2

class MenuSlider(override var origin: Vec2, size: Vec2, low: Float, high: Float, value: Float, val action: (Float) -> Unit): OutlinedSlider(size, low, high, value, LittleWindow.INSTANCE) {

    override val pointer: SliderPointer = BasicSliderPointer(this, 1f)

    override fun moveSliderTo(move: Float) {
        super.moveSliderTo(move)
        action(value)
    }
}