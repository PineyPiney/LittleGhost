package com.pineypiney.little_ghost.objects.menu_items

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.menu_items.scroll_lists.ScrollerText
import com.pineypiney.game_engine.objects.menu_items.scroll_lists.SelectableScrollingListEntry
import com.pineypiney.game_engine.objects.menu_items.scroll_lists.SelectableScrollingListItem
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.little_ghost.LittleWindow
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

    class KeyBindMenuEntry(parent: KeyBindList, number: Int, val key: ResourceKey): SelectableScrollingListEntry<KeyBindList>(parent, number) {

        private var binding: KeyBind = KeyBinds.getKeyBinding(key)

        var text: ScrollerText = ScrollerText(binding.bind.toString(), LittleWindow.INSTANCE, size * Vec2(0.3, 0.8), limits, defaultColour)

        override fun init() {
            super.init()
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
            text.colour = selectedColour
        }

        override fun unselect(){
            super.unselect()
            text.colour = defaultColour
        }

        override fun draw() {
            super.draw()
            text.drawCenteredLeft(origin + (size * Vec2(0.05, 0.5)))
        }

        override fun updateAspectRatio(window: Window) {
            text.updateAspectRatio(window)
        }

        override fun delete() {
            text.delete()
        }

        companion object {
            val defaultColour = Vec4(0, 0, 0, 1)
            val selectedColour = Vec4(1)
        }
    }
}