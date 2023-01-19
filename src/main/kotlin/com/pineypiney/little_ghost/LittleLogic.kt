package com.pineypiney.little_ghost

import com.pineypiney.game_engine.GameLogic
import com.pineypiney.game_engine.objects.Interactable
import com.pineypiney.game_engine.rendering.cameras.OrthographicCamera
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.delete
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.little_ghost.audio.Audio
import com.pineypiney.little_ghost.renderers.PixelRenderer
import com.pineypiney.little_ghost.util.KeyBinds
import org.lwjgl.openal.AL10

abstract class LittleLogic(final override val gameEngine: LittleEngine) : GameLogic() {

    final override val camera: OrthographicCamera = OrthographicCamera(gameEngine.window)
    abstract override val renderer: PixelRenderer<*>

    val sounds: MutableSet<Audio> = mutableSetOf()

    fun play(audio: Audio, loop: Boolean = false){
        sounds.add(audio)
        audio.src.loop = loop
        audio.src.play()
    }

    override fun open() {
        super.open()
        AL10.alSourcePlayv(sounds.filter { it.src.state == AL10.AL_PAUSED }.map { it.src.ptr }.toIntArray())
    }

    open fun close(){
        AL10.alSourcePausev(sounds.map { it.src.ptr }.toIntArray())
    }

    override fun update(interval: Float, input: Inputs) {
        super.update(interval, input)

        sounds.filter { !it.src.loop && it.src.state == AL10.AL_STOPPED }.forEach {
            it.src.delete()
            sounds.remove(it)
        }
    }

    open fun updateVolume(){
        sounds.forEach { it.updateVolume() }
    }

    override fun onInput(state: InputState, action: Int): Int {
        if(KeyBinds.isActivatedBy(ResourceKey("key/fullscreen"), state) && action == 1) toggleFullscreen()

        return super.onInput(state, action)
    }

    open fun regenerateFrameBuffers(){
        renderer.regenerateFrameBuffers()
    }

    override fun setFullscreen(state: Boolean) {
        super.setFullscreen(state)
        regenerateFrameBuffers()
    }

    override fun cleanUp() {
        sounds.map { it.src }.delete()
    }

    companion object{
        const val INTERRUPT = Interactable.INTERRUPT
    }
}