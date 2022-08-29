package com.pineypiney.little_ghost.objects.entities.characters

import com.pineypiney.game_engine.audio.AudioSource
import com.pineypiney.game_engine.objects.Collidable
import com.pineypiney.game_engine.objects.Updateable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.ModelledGameObject2D
import com.pineypiney.game_engine.objects.util.collision.CollisionBox2D
import com.pineypiney.game_engine.objects.util.collision.HardCollisionBox
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.coerceIn
import com.pineypiney.game_engine.util.extension_functions.copy
import com.pineypiney.game_engine.util.extension_functions.round
import com.pineypiney.little_ghost.screens.LittleGameScene
import com.pineypiney.little_ghost.screens.MovementMechanics
import glm_.detail.Random
import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW.glfwGetTime
import kotlin.math.abs

class ModelledCharacter(var scene: LittleGameScene, key: ResourceKey, debug: Int = 0): ModelledGameObject2D(key, debug), Updateable, Collidable {

    private val m: MovementMechanics; get() = scene.movement

    var minPos = Vec2(-Float.MAX_VALUE)
    var maxPos = Vec2(Float.MAX_VALUE)

    override val collider: CollisionBox2D = model.collisionBox.copy()

    // Keep the velocity within the bounds set by the scene the character is in
    override var velocity: Vec2 = Vec2()
        set(value) {
            // --- Jumping Mechanics ---
            // If the character is on the floor then the vertical velocity should not be less than 0
            if(position.y == minPos.y && value.y < 0) {
                value.y = 0f
            }

            // --- Running Mechanics ---
            // The x value of scale is set to +-1 to change the way the character is facing
            // Only change it if the character is moving
            if(value.x != 0f){
                val d = if(value.x > 0) 1 else -1
                scale.x = abs(scale.x) * d
            }

            // Rounding to within 0.01 stops friction from reducing velocity to infinitesimal amounts
            field = value.coerceIn(Vec2(m.maxSpeed, m.terminalV)).round(0.01f)
        }

    var walking: Boolean = false
        set(value) {

            // Reset animation offset so that the animation restarts in the right place
            // (value && !field) checks that field (moving) is being set true but is currently false
            if(value && !field) {
                //setAnimation("walking")
            }
            field = value
        }

    val jumpSound = AudioLoader[ResourceKey("broke")]

    var sprinting: Boolean = false

    private var lastJumpTime: Double = 0.0
    private val grounded: Boolean; get(){
        return if(velocity.y == 0f){

            // If moving the collision box down by a small amount causes it to have an
            // upwards ejection vector with any of its collisions, then it is grounded
            val newCollision = collider.copy()
            newCollision.origin += Vec2(0, -0.001) / this.scale
            for(c in objects.flatMap { it.getAllCollisions() }.toSet()){
                if (newCollision.getEjectionVector(c).y > 0) return true
            }
            false
        }
        else false

    }

    override fun init() {
        super.init()
        collider.parent = this
    }

    override fun update(interval: Float, time: Double) {

        // Velocity = Acceleration * Time
        velocity = (velocity + (m.gravity * interval)) * Vec2((1f - m.friction), 1)

        // Position = Velocity * Time
        translate(move((velocity * interval)))
    }

    fun jump(){
        if(grounded) {
            lastJumpTime = glfwGetTime()

            AudioSource(jumpSound).apply { gain = Random.float }.play()
        }
        if(glfwGetTime() < lastJumpTime + 0.5){
            velocity = velocity + Vec2(0, 2)
        }
    }

    fun accelerate(direction: Int, interval: Float, multiplier: Float){
        velocity plusAssign Vec2(direction * multiplier * interval, 0)
    }


    fun move(movement: Vec2): Vec2 {
        val collidedMove = movement.copy()

        // Create a temporary collision box in the new position to calculate collisions
        val newCollision = collider.copy()
        newCollision.origin += (movement / this.scale)

        // Iterate over all collision boxes sharing object collections and
        // eject this collision boxes object if the collision boxes collide
        for(it in objects){
            val collisions = it.getAllCollisions()
            for(box in collisions){
                if(box != collider && box is HardCollisionBox) collidedMove plusAssign  newCollision.getEjectionVector(box)
            }
        }

        // If a collision is detected in either direction then set the velocity to 0
        if(collidedMove.x != movement.x) velocity.x = 0f
        if(collidedMove.y != movement.y) velocity.y = 0f

        return collidedMove
    }


    override fun shouldUpdate(): Boolean = true
}