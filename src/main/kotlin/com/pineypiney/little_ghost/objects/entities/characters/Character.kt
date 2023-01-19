package com.pineypiney.little_ghost.objects.entities.characters

import com.pineypiney.game_engine.audio.AudioSource
import com.pineypiney.game_engine.objects.Interactable
import com.pineypiney.game_engine.objects.game_objects.objects_2D.AnimatedObject2D
import com.pineypiney.game_engine.objects.util.collision.HardCollisionBox
import com.pineypiney.game_engine.objects.util.collision.SoftCollisionBox
import com.pineypiney.game_engine.objects.util.shapes.Shape
import com.pineypiney.game_engine.resources.audio.AudioLoader
import com.pineypiney.game_engine.resources.shaders.Shader
import com.pineypiney.game_engine.resources.textures.Texture
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.*
import com.pineypiney.game_engine.util.maths.shapes.Rect3D
import com.pineypiney.game_engine.util.raycasting.Ray
import com.pineypiney.little_ghost.screens.LittleGameScene
import com.pineypiney.little_ghost.screens.MovementMechanics
import glm_.detail.Random
import glm_.f
import glm_.func.common.abs
import glm_.i
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW.glfwGetTime
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sign

abstract class Character(var scene: LittleGameScene, override val id: ResourceKey, width: Float = 1f): AnimatedObject2D(
    defaultShader), Interactable {

    override val children: MutableSet<Interactable> = mutableSetOf()
    override val importance: Int = 0
    override var forceUpdate: Boolean = false
    override var hover: Boolean = false
    override var pressed: Boolean = false

    private val m: MovementMechanics; get() = scene.movement
    open val collider = SoftCollisionBox(this, Vec2(-0.5), Vec2(1))

    val levelFloor: Float get() = scene.floorAt(position.x)

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
                animationOffset = glfwGetTime().f
            }
            field = value
        }

    var sprinting: Boolean = false

    abstract var sprintingFrames: List<Texture>

    override var currentFrame: Texture = Texture.broke
    override val animationLength: Float = 1.1f
    override var animationOffset: Float = glfwGetTime().f

    var minPos = Vec2(-Float.MAX_VALUE)
    var maxPos = Vec2(Float.MAX_VALUE)

    override var shader: Shader
        get() = super.shader
        set(_) {}

    private var lastJumpTime: Double = 0.0
    private val grounded: Boolean; get(){
        return if(velocity.y == 0f){

            if(collider.box.points.minOf { it.y } - levelFloor < 0.01f) return true
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

    val jumpSound = AudioLoader[(ResourceKey("broke"))]

    init {
        scale.x = width
    }

    override fun calcCurrentFrame(): Texture {
        val progress = ((glfwGetTime() - animationOffset) / animationLength) % 1

        currentFrame = if(walking){
            if(sprinting) sprintingFrames[(progress * sprintingFrames.size).i]
            else movingFrames[(progress * movingFrames.size).i]
        }
        else {
            stillFrames[(progress * stillFrames.size).i]
        }

        scale = Vec2(scale.x, abs(scale.x) / currentFrame.aspectRatio)

        // Returns current frame
        return currentFrame
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)

        Shape.centerSquareShape2D.apply {
            bind()
            draw()
        }
    }

    override fun update(interval: Float, time: Double) {

        // Velocity = Acceleration * Time
        velocity = (velocity + (m.gravity * interval)) * Vec2((1f - m.friction), 1)

        // Position = Velocity * Time
        translate(move((velocity * interval)))
        val floor = levelFloor
        val bottom = collider.box.points.minOf { it.y }
        if(bottom < floor){
            translate(Vec2(0, floor - bottom))
            velocity.y = 0f
        }
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
        newCollision.origin += (movement / scale)

        // Iterate over all collision boxes sharing object collections and
        // eject this collision boxes object if the collision boxes collide
        i@for(it in objects){
            val collisions = it.getAllCollisions().filterIsInstance<HardCollisionBox>()
            for(box in collisions){
                if(!newCollision.collidesWith(box)) continue
                val normals = box.box.normals(newCollision.box)
                // The shortest exit vector from the collider paired with the distance
                val se = normals.associateWith { newCollision.box.overlap1D(it, box.box) }.minBy { it.value.abs }

                val dir = collider.box.normals().associateWith { it dot se.key }.maxBy{ it.value.abs }.run { key * value * se.value.sign }
                val angle = (dir.angle() - (-se.key).angle()).wrap(0f, 2 * PI.f)

                val l = se.value / cos(angle).abs
                val e = dir * l.abs
                val testCollision = newCollision.copy()
                testCollision.origin += (e / scale)
                collidedMove plusAssign e
            }
        }

        // If a collision is detected in either direction then set the velocity to 0
        if(collidedMove.x != movement.x) velocity.x = 0f
        if(collidedMove.y != movement.y) velocity.y = 0f

        return collidedMove
    }

    override fun checkHover(ray: Ray, screenPos: Vec2): Boolean {
        val rect = Rect3D(position - scale/2, scale)
        val h = ray.passesThroughRect(rect)
        return h
    }

    override fun toString(): String {
        return "Character[${id.key}]"
    }
}