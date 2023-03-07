package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.VecCloud

@Suppress("unused")
object Monitor {
    private val dataCache = mutableMapOf<String, TextData<*>>()
    private val clouds: MutableMap<String, VecCloud> = mutableMapOf()
    val dictVec = WordVec()

    fun add(text: TextData<*>, cloud: String): Monitor{
        dictVec.createAndAddVec(text)
        this + Pair(text, cloud)
        updateClouds()
        return this
    }

    fun remove(text: TextData<*>, cloud: String): Monitor{
        clouds[cloud]?.minus(text)
        updateClouds()
        return this
    }

    fun addAll(list: List<Pair<TextData<*>, String>>): Monitor{
        list.forEach { (textData, cloudName) ->
            dictVec.createAndAddVec(textData)
            this + Pair(textData, cloudName)
        }
        updateClouds()
        return this
    }

    fun clear() {
        clouds.clear()
        dictVec.clear()
    }

    private operator fun plus(pair: Pair<TextData<*>, String>):Monitor{
        if(!clouds.containsKey(pair.second)) {
            clouds[pair.second] = VecCloud()
        }
        clouds[pair.second]!! + pair.first
        return this
    }

    private fun findClosestCloud(data: TextData<*>): String {
        dictVec.createVec(data)
        return clouds.minByOrNull { it.value.closestDistance(data) }?.key ?: "default"
    }

    private fun updateClouds() = clouds.forEach { (_, cloud) ->
        cloud.cloud.forEach { it.vector?.update() }
    }

    /* Cloud Ops */
    fun cloudKeys() = clouds.keys
    operator fun get(cloud: String): VecCloud? {
        return clouds[cloud]
    }
    fun cloud(cloud: String) {
        clouds[cloud] = VecCloud()
    }
    fun renameCloud(oldName: String, newName: String) {
        if(clouds.containsKey(oldName)) {
            clouds[newName] = clouds[oldName]!!
            clouds.remove(oldName)
        }
    }
    operator fun contains(cloud: String): Boolean {
        return clouds.containsKey(cloud)
    }

    /* Cache Ops */
    fun cacheKeys() = dataCache.keys
    fun loadFromCache(key: String) = dataCache[key]
    fun addToCache(key: String, data: TextData<*>) = dataCache.put(key, data)
    fun removeFromCache(sampleName: String) {
        dataCache.remove(sampleName)
    }
}