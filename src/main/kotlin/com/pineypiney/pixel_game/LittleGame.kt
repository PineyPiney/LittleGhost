package com.pineypiney.pixel_game

import com.pineypiney.pixel_game.screens.MenuScreen

var gameEngine = LittleEngine(LittleWindow.INSTANCE)

fun main(vararg args: String) {
    gameEngine.run()
}

fun setGame(game: LittleLogic, delete: Boolean = true){
    gameEngine.setGame(game, delete)
}
// Delete is set false when making a sub menu, and the current menu
// will be brought back once the player backs out of that menu
fun setMenu(newMenu: MenuScreen, delete: Boolean = true){
    gameEngine.setMenu(newMenu, delete)
}
fun openGame(init: Boolean = true){
    gameEngine.openGame(init)
}
fun openMenu(init: Boolean = true){
    gameEngine.openMenu(init)
}