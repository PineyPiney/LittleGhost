package com.pineypiney.little_ghost.util

import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.objects.Initialisable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.GameObject2D
import com.pineypiney.game_engine.util.extension_functions.replaceWhiteSpaces
import com.pineypiney.little_ghost.objects.menu_items.dialogue.ChoiceDialogue
import com.pineypiney.little_ghost.objects.menu_items.dialogue.DialogueBubble
import com.pineypiney.little_ghost.objects.menu_items.dialogue.SimpleDialogue
import com.pineypiney.little_ghost.screens.LittleGameScene
import glm_.f
import glm_.i
import glm_.vec2.Vec2
import java.io.File

class ScriptProcessor(val game: LittleGameScene, script: File): Initialisable {

    constructor(game: LittleGameScene, script: String): this(game, File("src/main/resources/dialogue/$script.dlg"))

    var currentBox: DialogueBubble? = null
    var speaker: GameObject2D? = null

    var line = 0
    val lines = script.readLines().filter { it.isNotEmpty() }

    // Choice Depth is how many choices deep the current line is, so that choices can be nested
    var choiceDepth = 0
    // Choice Indices is a map of choice depth to the indices of those choices,
    // which includes the indices of each option as well as the end of the choice
    var choiceIndices: MutableMap<Int, IntArray> = mutableMapOf()

    var end = false

    override fun init() {
        process(lines[0])
        currentBox?.startTime = Timer.frameTime.f
    }

    fun update() {
        currentBox?.delete()
        line++
        if(line >= lines.size){
            end = true
            delete()
            return
        }
        process(lines[line])
    }

    fun process(line: String){
        // This line is the end of the most recent choice,
        // so the reader should skip to the end of this choice
        if(line.trimStart().startsWith('}')) {
            this.line = choiceIndices[choiceDepth]?.last() ?: 0
            choiceDepth--
            update()
        }
        // This line is a comment so can be skipped
        else if(line.trimStart().startsWith('#')){
            update()
        }
        // Beat shouldn't do anything rn
        else if(line.trimStart().startsWith("Beat")){
            update()
        }
        // Otherwise, it is a normal instruction and should be parsed as such
        else{
            val (instr, dets) = line.split(':')
            when(instr.replaceWhiteSpaces()){
                "Choice" -> addDialogue(choice(dets.split('/').map { between(it) }))
                "Jump" -> jump(between(dets))
                "Beat" -> addDialogue(beat(dets.replaceWhiteSpaces().i))
                else -> addDialogue(say(instr.replaceWhiteSpaces(), between(dets)))
            }
        }
    }

    fun addDialogue(dialogue: DialogueBubble){
        speaker?.let { dialogue.position = it.position + Vec2(-it.scale.x / 2, it.scale.y / 2) }
        dialogue.init()

        currentBox = dialogue
        game.add(currentBox)
    }

    fun choice(choices: List<String>): ChoiceDialogue{
        return ChoiceDialogue(this, ++choiceDepth, line, choices)
    }

    fun jump(file: String){
        val newScript = ScriptProcessor(game, File("src/main/resources/dialogue/$file.dlg"))
        delete()
        newScript.init()
    }

    fun beat(time: Int): SimpleDialogue{
        return SimpleDialogue(this, "", "...$time")
    }

    fun say(character: String, say: String): SimpleDialogue{
        val speaker = game.gameObjects.gameItems.firstOrNull { it.id.key.replace("_", "") == character.lowercase() }
        if(speaker is GameObject2D) this.speaker = speaker
        return SimpleDialogue(this, character, say)
    }

    override fun delete() {
        currentBox?.delete()
        game.inScript = false
        game.startScript = Timer.frameTime
    }

    companion object{
        fun between(string: String, char: Char = '"') = string.substringAfter(char).substringBeforeLast(char)
    }
}