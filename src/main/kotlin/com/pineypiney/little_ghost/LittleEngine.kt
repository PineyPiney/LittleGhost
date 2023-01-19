package com.pineypiney.little_ghost

import com.pineypiney.game_engine.GameEngine
import com.pineypiney.game_engine.resources.FileResourcesLoader
import com.pineypiney.game_engine.resources.text.FontLoader
import com.pineypiney.little_ghost.screens.LittleGameScene
import com.pineypiney.little_ghost.screens.MainMenuScreen
import com.pineypiney.little_ghost.screens.MenuScreen
import com.pineypiney.little_ghost.util.UserSettings
import glm_.vec2.Vec2i
import mu.KotlinLogging

class LittleEngine: GameEngine<LittleLogic>(FileResourcesLoader("src/main/resources")) {

    override val window: LittleWindow = LittleWindow.INSTANCE
    override var TARGET_FPS: Int = 1000
    override val TARGET_UPS: Int = 20

    init {
        defaultFont = "theemy_font"
        FontLoader.INSTANCE.loadFontWithTexture("theemy_font.bmp", resourcesLoader, 75, 150, 0.04f)
        FontLoader.INSTANCE.loadFontFromTTF("LightSlab.ttf", resourcesLoader)
        FontLoader.INSTANCE.loadFontFromTTF("SemiSlab.ttf", resourcesLoader)
    }

    var menu: MenuScreen = MainMenuScreen(this)
    var game: LittleLogic = LittleGameScene(this)

    override var activeScreen: LittleLogic = menu

    override fun init() {
        UserSettings.loadOptions()
        super.init()
        resourcesLoader.getStream("cursor.png")?.let {
            window.setCursor(it, Vec2i(8, 0))
        }
        setMenu(MainMenuScreen(this))
        openMenu()
    }

    fun setGame(game: LittleLogic, delete: Boolean = true){
        if(delete) this.game.cleanUp()
        this.game = game
    }
    // Delete is set false when making a sub menu, and the current menu
    // will be brought back once the player backs out of that menu
    fun setMenu(newMenu: MenuScreen, delete: Boolean = true){
        if(delete) menu.cleanUp()
        menu = newMenu
    }
    fun openGame(init: Boolean = true){
        if(init) game.init()
        activeScreen.close()
        game.open()
        activeScreen = game
    }
    fun openMenu(init: Boolean = true){
        if(init) menu.init()
        menu.open()
        activeScreen = menu
    }

    companion object{
        val logger = KotlinLogging.logger("Pixel Game")
    }
}