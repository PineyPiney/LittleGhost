package com.pineypiney.little_ghost.objects.decorative

import com.pineypiney.game_engine.objects.Updateable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.SimpleTexturedGameObject2D
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.little_ghost.gameEngine
import com.pineypiney.little_ghost.screens.LittleGameScene
import glm_.detail.Random
import glm_.vec2.Vec2

class Cloud(name: ResourceKey, textureKey: ResourceKey, shape: Shape = Shape.centerSquareShape2D, shader: Shader = defaultShader): SimpleTexturedGameObject2D(name, TextureLoader.getTexture(textureKey), shape, shader), Updateable {

    var speed: Float = (Random.float - 0.5f) * 5

    override fun update(interval: Float, time: Double) {
        translate(Vec2(speed * interval, 0))
        val screen = gameEngine.activeScreen
        if(screen is LittleGameScene){
            // d is the total distance the cloud travels, the width of the
            // level + the cloud's width to ensure it is off the screen
            val d = screen.width + scale.x
            if(this.position.x > d / 2){
                this.translate(Vec2(-d, 0))
            }
            else if(this.position.x < -d / 2){
                this.translate(Vec2(d, 0))
            }
        }
    }

    override fun shouldUpdate(): Boolean = true
}