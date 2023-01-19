package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.objects.menu_items.MenuSlider
import com.pineypiney.little_ghost.objects.menu_items.TextButton
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2

class AudioMenuScreen(parent: MenuScreen, gameEngine: LittleEngine) : SubMenuScreen(parent, gameEngine) {

    private val masterText = SizedStaticText("Master Volume: ", window, 10)
    private val musicText = SizedStaticText("Music Volume: ", window, 10)
    private val sfxText = SizedStaticText("SFX Volume: ", window, 10)

    private val masterVolume = MenuSlider(Vec2(-0.1, 0.85), Vec2(0.8, 0.1), 0f, 200f, UserSettings.masterVolume * 100){
        UserSettings.masterVolume = it / 100
    }

    private val musicVolume = MenuSlider(Vec2(-0.1, 0.65), Vec2(0.8, 0.1), 0f, 100f, UserSettings.musicVolume * 100){
        UserSettings.musicVolume = it / 100
    }

    private val sfxVolume = MenuSlider(Vec2(-0.1, 0.45), Vec2(0.8, 0.1), 0f, 100f, UserSettings.sfxVolume * 100){
        UserSettings.sfxVolume = it / 100
    }

    val backButton = TextButton("Back", Vec2(-0.4, -0.9), 0.3f, 0xF5E5DF, 0.8f){
        setMenu(this.parent)
        openMenu(false)
    }

    override fun init() {
        super.init()

        masterText.init()
        musicText.init()
        sfxText.init()
    }

    override fun addObjects() {
        super.addObjects()

        add(masterVolume)
        add(musicVolume)
        add(sfxVolume)
        add(backButton)
    }

    override fun render(window: Window, tickDelta: Double) {
        super.render(window, tickDelta)

        masterText.drawCenteredLeft(Vec2(-0.7, 0.9))
        musicText.drawCenteredLeft(Vec2(-0.7, 0.7))
        sfxText.drawCenteredLeft(Vec2(-0.7, 0.5))
    }

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)

        masterText.updateAspectRatio(window)
        musicText.updateAspectRatio(window)
        sfxText.updateAspectRatio(window)
    }

    override fun cleanUp() {
        super.cleanUp()

        masterText.delete()
        musicText.delete()
        sfxText.delete()
    }
}