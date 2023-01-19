package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.scroll_lists.SelectableScrollingListEntry
import com.pineypiney.game_engine.objects.menu_items.scroll_lists.SelectableScrollingListItem
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.little_ghost.util.KeyBind
import com.pineypiney.little_ghost.util.KeyBinds
import glm_.vec2.Vec2
import glm_.vec4.Vec4

class KeyBindList(override var origin: Vec2, override val size: Vec2, override val entryHeight: Float, override val scrollerWidth: Float) :
    SelectableScrollingListItem() {

    override val items: List<KeyBindMenuEntry> = createKeys()

    override val action: (Int, SelectableScrollingListEntry<*>?) -> Unit = { _, _ ->  }

    // Overriding this function allows it to return this specific class
    override fun getSelectedEntry(): KeyBindMenuEntry? = items.getOrNull(selectedEntry)

    fun setKeyBind(bind: InputState){
        val entry = getSelectedEntry()

        entry?.setKeyBind(bind)

        selectedEntry = -1
    }

    private fun createKeys(): List<KeyBindMenuEntry> {
        return KeyBinds.keyBinds.keys.mapIndexed { i, k ->
            KeyBindMenuEntry(this, i, k)
        }
    }

    class KeyBindMenuEntry(parent: KeyBindList, number: Int, private val key: ResourceKey): SelectableScrollingListEntry<KeyBindList>(parent, number) {

        private var binding: KeyBind = KeyBinds.getKeyBinding(key)

        var nameText = SizedScrollerText(KeyBinds.keyBindNames[key] ?: "Unnamed", 20, limits, defaultColour)
        var text: SizedScrollerText = SizedScrollerText(binding.bind.toString(), 20, limits, defaultColour)

        override fun init() {
            super.init()
            nameText.init()
            text.init()
        }

        private fun updateText(){
            text.text = binding.bind.toString()
        }

        private fun setKeyBind(bind: KeyBind){

            this.binding = bind
            updateText()

            KeyBinds.setKeyBind(key, bind)
            unselect()
        }

        fun setKeyBind(bind: InputState) = setKeyBind(KeyBind(bind))

        override fun select(){
            super.select()
            nameText.colour= selectedColour
            text.colour = selectedColour
        }

        override fun unselect(){
            super.unselect()
            nameText.colour = defaultColour
            text.colour = defaultColour
        }

        override fun draw() {
            super.draw()
            nameText.drawCenteredLeft(origin + (size * Vec2(0.05, 0.5)))
            text.drawCenteredRight(origin + (size * Vec2(0.95, 0.5)))
        }

        override fun updateAspectRatio(window: Window) {
            nameText.updateAspectRatio(window)
            text.updateAspectRatio(window)
        }

        override fun delete() {
            nameText.delete()
            text.delete()
        }

        companion object {
            val defaultColour = Vec4(0, 0, 0, 1)
            val selectedColour = Vec4(1)
        }
    }
}