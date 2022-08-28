package com.pineypiney.pixel_game

import com.pineypiney.game_engine.GameEngine
import com.pineypiney.game_engine.IGameLogic
import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.resources.FileResourcesLoader
import com.pineypiney.pixel_game.screens.LittleGameScene
import com.pineypiney.pixel_game.screens.MainMenuScreen
import com.pineypiney.pixel_game.screens.MenuScreen
import com.pineypiney.pixel_game.util.UserSettings
import mu.KotlinLogging

class LittleEngine(window: Window): GameEngine(window, FileResourcesLoader("src/main/resources")) {

    override var TARGET_FPS: Int = 1000
    override val TARGET_UPS: Int = 20

    var game: LittleLogic = LittleGameScene(this)
    var menu: MenuScreen = MainMenuScreen(this)

    override var activeScreen: IGameLogic = menu

    override fun init() {
        UserSettings.loadOptions()
        super.init()
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
        (activeScreen as LittleLogic).close()
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