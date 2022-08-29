package com.pineypiney.little_ghost

import com.pineypiney.little_ghost.screens.MenuScreen

// This is needed to initialise the Window, which in turn initialises OpenGL
var window = LittleWindow.INSTANCE
var gameEngine = LittleEngine()

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