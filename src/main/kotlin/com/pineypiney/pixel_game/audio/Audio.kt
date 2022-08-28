package com.pineypiney.pixel_game.audio

import com.pineypiney.game_engine.audio.AudioSource
import org.lwjgl.openal.AL10

class Audio(src: com.pineypiney.game_engine.resources.audio.Audio, val type: AudioType, volume: Float = 1f, pitch: Float = 1f) {
    val src = AudioSource(src)

    var volume: Float = 0f
        set(value) {
            field = value
            updateVolume()
        }
    var pitch: Float
        get() = src.pitch
        set(value) {
            src.pitch = value
        }

    var minGain: Float
        get() = AL10.alGetSourcef(src.ptr, AL10.AL_MIN_GAIN)
        set(value) = AL10.alSourcef(src.ptr, AL10.AL_MIN_GAIN, value)
    var maxGain: Float
        get() = AL10.alGetSourcef(src.ptr, AL10.AL_MAX_GAIN)
        set(value) = AL10.alSourcef(src.ptr, AL10.AL_MAX_GAIN, value)

    init {
        maxGain = 2f
        this.pitch = pitch
        this.volume = volume
    }

    fun updateVolume(){
        src.gain = volume * type.value
    }
}