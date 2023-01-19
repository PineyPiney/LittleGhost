package com.pineypiney.little_ghost.screens

import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.objects.game_objects.GameObject
import com.pineypiney.game_engine.objects.text.SizedStaticText
import com.pineypiney.game_engine.resources.ResourcesLoader
import com.pineypiney.game_engine.resources.textures.TextureLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.extension_functions.lerp
import com.pineypiney.game_engine.util.extension_functions.roundedString
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.game_engine.util.input.Inputs
import com.pineypiney.little_ghost.LittleEngine
import com.pineypiney.little_ghost.LittleLogic
import com.pineypiney.little_ghost.objects.GameObjects
import com.pineypiney.little_ghost.objects.decorative.Background
import com.pineypiney.little_ghost.objects.decorative.FireFly
import com.pineypiney.little_ghost.objects.entities.characters.InteractableCharacter
import com.pineypiney.little_ghost.objects.entities.characters.LittleGhostCharacter
import com.pineypiney.little_ghost.objects.entities.characters.StillCharacter
import com.pineypiney.little_ghost.objects.util.BarrierObject
import com.pineypiney.little_ghost.openMenu
import com.pineypiney.little_ghost.renderers.PixelSceneRenderer
import com.pineypiney.little_ghost.resources.level.LevelDetails
import com.pineypiney.little_ghost.resources.level.LevelImporter
import com.pineypiney.little_ghost.resources.level.ObjectState
import com.pineypiney.little_ghost.setMenu
import com.pineypiney.little_ghost.util.KeyBinds
import com.pineypiney.little_ghost.util.ScriptProcessor
import glm_.f
import glm_.func.common.abs
import glm_.func.common.floor
import glm_.i
import glm_.pow
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import kool.lib.toList
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.glClearColor
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

class LittleGameScene(gameEngine: LittleEngine, val name: String = "EXAMPLE") : LittleLogic(gameEngine) {

    override val renderer: PixelSceneRenderer = PixelSceneRenderer()

    // Properties of the level
    var width: Float
    val height: Float = 10f
    private val hh get() = height / 2

    init {
        val stream = gameEngine.resourcesLoader.getStream("levels/$name.pgl")
        val details: LevelDetails = if(stream == null){
            LevelDetails("", 50f, Vec3(1))
        }
        else{
            LevelImporter.getLevelInfo(this.name, stream)
        }

        width = details.width
        val colour = details.colour
        glClearColor(colour.r, colour.g, colour.b, 1f)

    }

    var inScript = false
    var startScript = 0.0

    var movement: MovementMechanics = MovementMechanics(Vec2(0, -20), 54f, 0.4f, 36f, Vec2(-0.32, 0.32))

    private val ben = LittleGhostCharacter(this, 5.6f)
    private val blake = InteractableCharacter(this, ResourceKey("blake"), TextureLoader[ResourceKey("characters/blake_sprite")], 2f){ action, _ ->
        if(action == 1 && !inScript){
            ScriptProcessor(this, "1").init()
            inScript = true
            startScript = Timer.frameTime
        }
    }
    private val juliet = StillCharacter(this, ResourceKey("juliet"), TextureLoader[ResourceKey("characters/juliet_sprite")], 2f)
    private val sammy = InteractableCharacter(this, ResourceKey("sammy"), TextureLoader[ResourceKey("characters/fwoog")], 2f){ action, _ ->
        if(action == 1 && !inScript){
            ScriptProcessor(this, "fwoog").init()
            inScript = true
            startScript = Timer.frameTime
        }
    }

    val afloor = generateFloor("backgrounds/cinder_fields/floor.png")
    private val floor = BarrierObject(Vec2(-width / 2, 6-hh), Vec2(width, 0.8f), 0.2f)
    private val leftBarrier = BarrierObject(Vec2(-(width / 2)-1, -hh), Vec2(1, height * 3))
    private val rightBarrier = BarrierObject(Vec2(width/2, -hh), Vec2(1, height * 3))

    private val fpsText = SizedStaticText("FPS: ${gameEngine.FPS}", window, 12, Vec2(0.4, 0.4))
    private val speedText = SizedStaticText(ben.velocity.roundedString(2).joinToString(), window, 12, Vec2(1, 0.2))
    private val posText = SizedStaticText(ben.position.roundedString(2).joinToString(), window, 12, Vec2(1, 0.2))

    private val cloud1 = GameObjects.cloud1
    private val cloud2 = GameObjects.cloud2
    private val cloud3 = GameObjects.cloud3
    private val cloud4 = GameObjects.cloud1

    private val sky = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/sky")])
    private val clouds = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/clouds")])
    private val ground = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/ground")])
    private val trees = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/trees")])
    private val bushes = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/bushes")])
    private val lamppost = Background(TextureLoader[ResourceKey("backgrounds/cinder_fields/lamppost")])
    private val flies = List(10){ FireFly(Vec2(4, -1), 6f, 1f) }

    override fun init() {
        super.init()

        width = sky.width

        // Setting back depth to 1 ensures it is rendered behind the bunny
        ben.depth = 0

//        ben.translate(Vec2(0, -3.2))
        blake.translate(Vec2(4, 0))
        juliet.translate(Vec2(-4, 0))
        sammy.translate(Vec2(8, 0))

        ben.minPos = Vec2(-(this.width - ben.scale.x) * 0.5f - 5, -5)
        ben.maxPos = Vec2((this.width - ben.scale.x) * 0.5f - 5, 10)

        updateCameraBounds()
        camera.range = Vec2(4, 100)

        cloud1.translate(Vec2(5, 4))
        cloud2.translate(Vec2(-12, 3.5))
        cloud3.translate(Vec2(16, 2))
        cloud4.translate(Vec2(0, 2.8))

        fpsText.init()
        posText.init()
        speedText.init()
    }

    override fun addObjects() {
        val stream = gameEngine.resourcesLoader.getStream("levels/$name.pgl") ?: return
        val items: MutableMap<String, MutableSet<ObjectState>> = LevelImporter.loadLevel(this.name, stream)
        for((itemName, states) in items){
            for(state in states){
                GameObjects.getObject(itemName)?.let {
                    it.position = state.position
                    it.rotation = state.rotation
                    it.scale = state.scale
                    it.depth = state.depth
                    //add(it)
                }
            }
        }

        sky.depth = 10
        clouds.depth = 9
        ground.depth = 8
        trees.depth = 7
        bushes.depth = -1
        lamppost.depth = -2

        addall(ben, leftBarrier, rightBarrier, cloud1, cloud2, cloud3, cloud4, sky, clouds, ground, trees, bushes, lamppost)
        addall(blake, juliet, sammy)

        flies.forEach {
            addall(it)
            it.width = 0.1f
        }
    }

    override fun render(window: Window, tickDelta: Double) {
        renderer.render(window, this, tickDelta)

        fpsText.drawTopLeft(Vec2(-1, 1))
        speedText.drawTopRight(Vec2(1, 1))
        posText.drawTopRight(Vec2(1, 0.8))
    }

    override fun update(interval: Float, input: Inputs) {
        super.update(interval, input)

        updateMovement(interval)

        // Update any text that needs changing
        fpsText.text = "FPS: ${gameEngine.FPS}"
        speedText.text = ben.velocity.roundedString(2).joinToString()
        posText.text = ben.position.roundedString(2).joinToString()

        // Zoom the camera in and out for dialogue
        val scriptAnimationTime = (Timer.frameTime - startScript).f.coerceIn(0f, 2f)
        camera.height =
            if(inScript) 10 - scriptAnimationTime
            else 8 + scriptAnimationTime

        // Face Blake towards Ben
        val rel = ben.position.x - blake.position.x
        blake.scale = Vec2(abs(blake.scale.x) * (-1).pow((rel < 0).i), blake.scale.y)
    }

    fun updateMovement(interval: Float) {

        var direction = 0

        // This will read the keys continually
        if(KeyBinds.isActive(ResourceKey("key/left"))){
            direction += -1
        }
        if(KeyBinds.isActive(ResourceKey("key/right"))){
            direction += 1
        }

        // Sprinting
        val sprinting = KeyBinds.isActive(ResourceKey("key/sprint"))
        val speed = movement.maxSpeed *
                if(sprinting) 2f
                else 1f

        // Move the character
        if(direction != 0){
            // If the player is actively moving then the sprinting animation
            // should play by default if the player is holding the sprint key
            ben.sprinting = ben.velocity.x.abs > 45 || sprinting
            ben.walking = true

            ben.accelerate(direction, interval, speed)
        }
        else{
            // If the player is not moving the character then the character
            // should go straight from running to stopping animations
            ben.sprinting = ben.velocity.x.abs > 75
            ben.walking = ben.sprinting
        }

        if(KeyBinds.isActive(ResourceKey("key/jump"))){
            ben.jump()
        }

        // Then try to move the camera to this new position,
        // which will also be restricted by parameters defined in the class
        camera.setPos(Vec3(ben.position, hh))
    }

    override fun updateAspectRatio(window: Window) {
        super.updateAspectRatio(window)
        val backgroundRatio = this.width/this.height
        if(window.aspectRatio < backgroundRatio) {
            updateCameraBounds()
        }

        fpsText.updateAspectRatio(window)
        speedText.updateAspectRatio(window)
        posText.updateAspectRatio(window)
    }

    fun updateCameraBounds(){
        // v is the distance the camera can travel before the edge leaves the background texture
        val v = (this.width - camera.getSpan().x) * 0.5f
        camera.cameraMinPos = Vec3(-v, 0, hh)
        camera.cameraMaxPos = Vec3(v, 0, hh)
        // Reset the cameraPos so that it considers the new values
        camera.setPos(camera.cameraPos)
    }

    // This function should be used for keys that are pressed repeatedly, e.g. attacking.
    // For movement or other keys that are held, see PixelGameScene#updateMovement
    override fun onInput(state: InputState, action: Int): Int {
        if(super.onInput(state, action) == INTERRUPT) return INTERRUPT

        if(state.i == GLFW.GLFW_KEY_ESCAPE && action == 0){
            setMenu(PauseMenuScreen(gameEngine))
            openMenu()
            return INTERRUPT
        }

        for(value in KeyBinds.keyBinds.values){
            if(value activatedBy state){
                value.state = action
            }
        }

        return action
    }

    fun addall(vararg objects: GameObject){
        objects.forEach { add(it) }
    }

    fun generateFloor(texture: String, sections: Int): List<BarrierObject>{

        val bytes = ResourcesLoader.ioResourceToByteBuffer(gameEngine.resourcesLoader.getStream("textures/$texture") ?: return listOf(), 1048576)
        val (buffer, info) = TextureLoader.loadImageFromMemory(bytes, true)
        if(buffer == null) return listOf()

        val rows = buffer.toList().chunked(info.z){ it.sum() }.chunked(info.x)
        val heights = FloatArray(info.x){ c -> (0 until info.y).reversed().first { r -> rows[r][c] != -3 }.f * height / info.y - (height/2) }

        val secLen = width / sections

        val s = List(sections){
            val h1 = heights[((heights.size.f - 1) * it / sections).i]
            val h2 = heights[((heights.size.f - 1) * (it + 1) / sections).i]

            val left = ((it.f / sections) - 0.5f) * width
            val angle = atan((h2 - h1) / secLen)
            val o = Vec2(left, h1) - Vec2(0, 1).rotate(angle)

            val length = sqrt((h2-h1).pow(2) + (secLen*secLen))

            val b = BarrierObject(o, Vec2(length, 1), -angle)
            b
        }

        return s
    }

    fun generateFloor(texture: String): FloatArray{

        val image = ImageIO.read(gameEngine.resourcesLoader.getStream("textures/$texture") ?: return floatArrayOf())
        return FloatArray(image.width){ (image.raster.dataBuffer.getElem(it).f / 255f - 0.5f) * height}
    }

    fun floorAt(x: Float): Float{
        val delta = (((x / width) + 0.5f) * afloor.size)
        val left = delta.floor.i.coerceIn(0, afloor.size - 2)
        return afloor[left].lerp(afloor[left + 1], delta.mod(1f))
    }

    override fun cleanUp() {
        super.cleanUp()

        fpsText.delete()
        speedText.delete()
        posText.delete()
    }
}