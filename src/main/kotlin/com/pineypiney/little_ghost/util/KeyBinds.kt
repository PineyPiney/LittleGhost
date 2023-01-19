package com.pineypiney.little_ghost.util

import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.getOrNull
import com.pineypiney.game_engine.util.input.ControlType
import com.pineypiney.game_engine.util.input.InputState
import glm_.s
import org.lwjgl.glfw.GLFW

class KeyBinds {

    companion object{

        private val defaultKeyBinds: Map<ResourceKey, KeyBind> = mapOf(
            Pair(ResourceKey("key/left"), KeyBind(GLFW.GLFW_KEY_A)),
            Pair(ResourceKey("key/right"), KeyBind(GLFW.GLFW_KEY_D)),
            Pair(ResourceKey("key/sprint"), KeyBind(GLFW.GLFW_KEY_LEFT_SHIFT)),
            Pair(ResourceKey("key/jump"), KeyBind(GLFW.GLFW_KEY_SPACE)),

            Pair(ResourceKey("key/primary"), KeyBind(GLFW.GLFW_MOUSE_BUTTON_1, ControlType.MOUSE)),
            Pair(ResourceKey("key/secondary"), KeyBind(GLFW.GLFW_MOUSE_BUTTON_2, ControlType.MOUSE)),

            Pair(ResourceKey("key/attack"), KeyBind(GLFW.GLFW_MOUSE_BUTTON_1, ControlType.MOUSE)),

            Pair(ResourceKey("key/fullscreen"), KeyBind(GLFW.GLFW_KEY_F11)),
        )

        val keyBinds: MutableMap<ResourceKey, KeyBind> = defaultKeyBinds.toMutableMap()

        fun isActive(id: ResourceKey): Boolean{
            val bind = keyBinds.getOrNull(id) ?: return false
            return bind.state > 0
        }

        fun isActivatedBy(id: ResourceKey, state: InputState): Boolean{
            val bind = keyBinds[id] ?: return false
            return bind.activatedBy(state)
        }

        private val modStates: MutableMap<Short, Boolean> = mutableMapOf(
            Pair(GLFW.GLFW_KEY_LEFT_SHIFT.s, false),
            Pair(GLFW.GLFW_KEY_LEFT_CONTROL.s, false),
            Pair(GLFW.GLFW_KEY_LEFT_ALT.s, false),
            Pair(GLFW.GLFW_KEY_LEFT_SUPER.s, false),
            Pair(GLFW.GLFW_KEY_CAPS_LOCK.s, false),
            Pair(GLFW.GLFW_KEY_NUM_LOCK.s, false),
        )

        fun setKeyBind(key: ResourceKey, bind: KeyBind){
            if(keyBinds[key] != null) keyBinds[key] = bind
            else{
                print("There is no keybinding for $key")
            }
        }

        @Throws(NoSuchElementException::class)
        fun getKeyBinding(key: ResourceKey): KeyBind{
            return keyBinds[key] ?: defaultKeyBinds[key] ?:
            throw NoSuchElementException("There is no key registered for $key")
        }

        fun getKeyBindingForKey(key: Short, type: ControlType): KeyBind?{
            return keyBinds.values.firstOrNull { bind -> bind.key == key && bind.controlType == type }
        }

        val keyBindNames = mapOf(
            ResourceKey("key/left") to "Left",
            ResourceKey("key/right") to "Right",
            ResourceKey("key/sprint") to "Sprint",
            ResourceKey("key/jump") to "Jump",
            ResourceKey("key/primary") to "Primary",
            ResourceKey("key/secondary") to "Secondary",
            ResourceKey("key/attack") to "Attack",
            ResourceKey("key/fullscreen") to "Fullscreen"
        )
    }
}