package com.pineypiney.little_ghost.resources.level

import com.pineypiney.game_engine.resources.models.DataSource
import com.pineypiney.game_engine.resources.models.getAttribute
import com.pineypiney.game_engine.resources.models.getSource
import com.pineypiney.game_engine.util.extension_functions.addToListOr
import com.pineypiney.little_ghost.LittleEngine
import glm_.f
import glm_.i
import glm_.min
import glm_.vec3.Vec3
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class LevelImporter private constructor(){

    companion object {

        private val xPath: XPath = XPathFactory.newInstance().newXPath()

        fun getLevelInfo(fileName: String, stream: InputStream): LevelDetails {
            val doc: Document = getDocument(stream)
            val path: XPath = xPath

            val detailsRoot = "PGL/level"
            val name = getStringAt("$detailsRoot/name", doc, path)
            val width: Float =
                try {
                    getStringAt("$detailsRoot/width", doc, path).f
                } catch (e: NumberFormatException) {
                    LittleEngine.logger.warn("Could not parse width in level $fileName")
                    e.printStackTrace()
                    17.78f
                }
            val colour: Vec3 =
                try {
                    val nums = getStringAt("$detailsRoot/colour", doc, path).split(" ").map { it.f }
                    Vec3(nums[0], nums[1], nums[2])
                } catch (e: Exception) {
                    LittleEngine.logger.warn("Could not parse colour in level $fileName")
                    e.printStackTrace()
                    Vec3(1)
                }

            return LevelDetails(name, width, colour)
        }

        fun loadLevel(name: String, stream: InputStream): MutableMap<String, MutableSet<ObjectState>> {
            val itemCollection = mutableMapOf<String, MutableSet<ObjectState>>()

            val doc = getDocument(stream)
            val path = xPath
            val layersRoot = "PGL/layers/layer"

            val layers = path.evaluate(layersRoot, doc, XPathConstants.NODESET) as NodeList

            val layerIDs: MutableSet<String> = mutableSetOf()
            for (x in 0 until layers.length) {

                val value = getAttribute(layers.item(x).attributes, "id", "")
                layerIDs.add(value)
            }

            for(id in layerIDs){

                val layer = try{
                    id.i
                }
                catch (e: java.lang.NumberFormatException){
                    LittleEngine.logger.warn("Could not parse layer $id in level $name")
                    e.printStackTrace()
                    continue
                }

                val itemsRoot = "$layersRoot[@id = $id]/item"

                val items = path.evaluate(itemsRoot, doc, XPathConstants.NODESET) as NodeList

                val itemIDs: MutableSet<String> = mutableSetOf()
                for (x in 0 until items.length) {
                    val value = getAttribute(items.item(x).attributes, "name", "")
                    itemIDs.add(value)
                }

                itemIDs.forEach item@ { item ->

                    val errMsg = ""

                    val itemRoot = "$itemsRoot[@name = '$item']"

                    // Read the Sources
                    val sources = DataSource.readAllDataFromXML("$itemRoot/source", doc, path)

                    val tranSource = getSource(errMsg, "translations", "$itemRoot/sampler/input[@semantic = 'TRANSLATION']", doc, path) ?: return@item
                    val rotSource = getSource(errMsg, "rotations", "$itemRoot/sampler/input[@semantic = 'ROTATION']", doc, path) ?: return@item
                    val scaleSource = getSource(errMsg, "scales", "$itemRoot/sampler/input[@semantic = 'SCALE']", doc, path) ?: return@item

                    val translationsSource = sources.firstOrNull { it pointedBy tranSource } ?: DataSource.EMPTY
                    val rotationsSource = sources.firstOrNull { it pointedBy rotSource} ?: DataSource.EMPTY
                    val scalesSource = sources.firstOrNull { it pointedBy scaleSource} ?: DataSource.EMPTY

                    val translationsList = translationsSource.createVec2Array()
                    val rotationsList = rotationsSource.createFloatArray("ROTATION")
                    val scalesList = scalesSource.createVec2Array()

                    val total = translationsList.size.min(rotationsList.size).min(scalesList.size)
                    for (i in 0 until total) {
                        val position = translationsList[i]
                        val rotation = rotationsList[i]
                        val scale = scalesList[i]

                        itemCollection.addToListOr(item, ObjectState(position, rotation, scale, layer)) {mutableSetOf()}
                    }
                }
            }

            return itemCollection
        }

        private fun getDocument(stream: InputStream): Document{
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
        }

        fun getStringAt(root: String, doc: Document, path: XPath = xPath): String{
            return path.evaluate(root, doc, XPathConstants.STRING) as String
        }
    }
}