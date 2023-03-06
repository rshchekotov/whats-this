package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.IntVec
import edu.tum.romance.whatsthis.math.VecCloud
import edu.tum.romance.whatsthis.util.WordCount

@Suppress("unused")
object Monitor {
    private val clouds: MutableMap<String, VecCloud> = mutableMapOf()
    private val dictVec = WordVec

    fun add(association: Pair<List<WordCount>, String>): Monitor{
        this + Pair(dictVec.createAndAddVec(association.first), association.second)
        updateClouds()
        return this
    }

    fun addAll(list: List<Pair<List<WordCount>, String>>): Monitor{
        for((vec, cloudName) in list) {
            this + Pair(dictVec.createAndAddVec(vec), cloudName)
        }
        updateClouds()
        return this
    }

    fun addNoCloud(v: List<WordCount>): Monitor{
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
    }

    fun findClosestCloud(vec: List<WordCount>): String {
        return findClosestCloud(dictVec.createVec(vec))
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