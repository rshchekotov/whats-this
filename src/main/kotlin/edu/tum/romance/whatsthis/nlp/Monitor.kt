package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.IntVec
import edu.tum.romance.whatsthis.math.VecCloud
import edu.tum.romance.whatsthis.util.WordCount

@Suppress("unused")
object Monitor {
    private val clouds: MutableMap<String, VecCloud> = mutableMapOf()
    val dictVec = WordVec()

    fun add(text: TextData<*>, cloud: String): Monitor{
        this + Pair(dictVec.createAndAddVec(text.vector), cloud)
        updateClouds()
        return this
    }

    fun addAll(list: List<Pair<TextData<*>, String>>): Monitor{
        for((textData, cloudName) in list) {
            this + Pair(dictVec.createAndAddVec(textData.vector), cloudName)
        }
        updateClouds()
        return this
    }

    /**Hidden for now, since user should not be able to add without specifying a cloud**/
    /*fun addNoCloud(v: List<WordCount>): Monitor{
        val vec = dictVec.createAndAddVec(v)
        this + Pair(vec, findClosestCloud(vec))
        updateClouds()
        return this
    }

    fun addAllNoCloud(vecs: List<List<WordCount>>): Monitor{
        for(v in vecs) {
            val vec = dictVec.createAndAddVec(v)
            this + Pair(vec, findClosestCloud(vec))
        }
        updateClouds()
        return this
    }*/

    fun findClosestCloud(vec: List<WordCount>): String {
        return findClosestCloud(dictVec.createVec(vec))
    }

    fun get(cloud: String): VecCloud? {
        return clouds[cloud]
    }

    fun clear() {
        clouds.clear()
        dictVec.clear()
    }

    private operator fun plus(pair: Pair<IntVec, String>):Monitor{
        if(!clouds.containsKey(pair.second)) {
            clouds[pair.second] = VecCloud()
        }
        clouds[pair.second]!! + pair.first
        return this
    }

    private fun findClosestCloud(vec: IntVec): String {
        var closestCloud = "default"
        var closestDistance = Double.MAX_VALUE
        for((cloudName, cloud) in clouds) {
            val distance = cloud.closestDistance(vec)
            if(distance < closestDistance) {
                closestDistance = distance
                closestCloud = cloudName
            }
        }
        return closestCloud
    }

    private fun updateClouds() {
        for((_, cloud) in clouds) {
            cloud.cloud.forEach { it.update() }
        }
    }

}