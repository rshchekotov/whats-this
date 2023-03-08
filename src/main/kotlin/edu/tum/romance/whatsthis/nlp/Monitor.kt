package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.IntVec

@Suppress("unused")
object Monitor {
    private val dataCache = mutableMapOf<String, TextData<*>>()
    private val clouds: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val dictVec = WordVec()

    //#region CRUD Ops
    fun add(name: String, data: TextData<*>, cloud: String? = null) {
        addToCache(name, data)
        if(cloud != null && cloud in clouds) {
            assign(name, cloud)
        }
    }

    fun assign(dataRef: String, cloud: String): Monitor{
        clouds.forEach {
            if(dataRef in it.value) {
                it.value -= dataRef
            }
        }

        if (clouds.containsKey(cloud)) {
            clouds[cloud]!! += dataRef
        } else {
            clouds[cloud] = mutableListOf(dataRef)
        }

        updateClouds()
        return this
    }

    fun revoke(dataRef: String, cloud: String): Monitor {
        clouds[cloud]?.remove(dataRef)
        updateClouds()
        return this
    }

    fun remove(text: String, cloud: String): Monitor {
        clouds[cloud]?.minus(text)
        updateClouds()
        return this
    }

    fun clear() {
        clouds.clear()
        dictVec.clear()
    }
    //#endregion

    //#region Math Stuff
    private fun findClosestCloud(data: TextData<*>, distance: Distance = EuclideanDistance): String {
        dictVec.createVec(data)
        return clouds.minByOrNull { cloud ->
            if(cloud.value.isEmpty()) return@minByOrNull Double.MAX_VALUE
            (cloud.value.map { dataCache[it] }.minByOrNull { distance(it?.vector!!, data.vector!!) }?.vector?.let {
                distance(it, data.vector!!)
            } ?: Double.MAX_VALUE)
        }?.key ?: "default"
    }
    //#endregion

    //#region Update Ops
    private fun updateClouds() = clouds.forEach { (_, cloud) ->
        cloud.mapNotNull { dataCache[it]?.vector }.forEach { updateIntVec(it) }
    }

    private fun updateIntVec(vec: IntVec) {
        val vecList = ArrayList<Int>(dictVec.dictionary.size)

        vecList.addAll(vec.data)
        for(i in vecList.size until dictVec.dictionary.size) {
            vecList.add(0)
        }
        vec.data = vecList
    }
    //#endregion

    //#region Cloud Ops
    fun cloudKeys() = clouds.keys
    operator fun get(cloud: String): List<String>? {
        return clouds[cloud]
    }
    fun createCloud(cloud: String) {
        clouds[cloud] = mutableListOf()
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
    //#endregion

    //#region Cache Ops
    fun cacheKeys() = dataCache.keys
    fun loadFromCache(key: String) = dataCache[key]
    private fun addToCache(key: String, data: TextData<*>) {
        if(dataCache.containsKey(key)) return
        dictVec.createAndAddVec(data)
        dataCache[key] = data
    }
    fun renameInCache(old: String, new: String) {
        if(dataCache.containsKey(old)) {
            dataCache[new] = dataCache[old]!!
            dataCache.remove(old)
        }
    }
    fun removeFromCache(sampleName: String) {
        dataCache.remove(sampleName)
    }
    //#endregion
}