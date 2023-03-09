package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.Vector

@Suppress("unused")
object Monitor {
    private val dataCache = mutableMapOf<String, TextData<*>>()
    private val clouds: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val unclassified = mutableListOf<String>()
    private val dictVec = WordVec()

    private val emptyVector
        get() = Vector(dictVec.dictionary.size)
    val vocabulary
        get() = dictVec.dictionary.toSet()

    //#region CRUD Ops
    fun add(name: String, data: TextData<*>, cloud: String? = null) {
        addToCache(name, data)
        if(cloud != null) {
            if(cloud !in clouds) {
                clouds[cloud] = mutableListOf()
            }
            assign(name, cloud)
        } else {
            unclassified += name
        }
    }

    fun assign(dataRef: String, cloud: String): Monitor{
        clouds.forEach {
            if(dataRef in it.value) {
                it.value -= dataRef
            }
        }

        if(dataRef in unclassified) {
            unclassified -= dataRef
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
        unclassified += dataRef
        updateClouds()
        return this
    }

    fun clear() {
        clouds.clear()
        dataCache.clear()
        dictVec.clear()
    }

    fun isEmpty(): Boolean = clouds.isEmpty() && dataCache.isEmpty() && dictVec.dictionary.isEmpty()
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

    fun summary(cloud: String): Vector {
        if(cloud in Monitor && Monitor[cloud]!!.isNotEmpty()) {
            val vec = emptyVector
            clouds[cloud]!!.forEach { vec += dataCache[it]!!.vector!! }
            vec.unit(EuclideanDistance)
            return vec
        }
        return emptyVector
    }

    fun significance(cloud: String): Vector {
        if(cloud in Monitor && Monitor[cloud]!!.isNotEmpty()) {
            val classVector = summary(cloud)

            val result = emptyVector
            for ((name, _) in clouds) {
                result += summary(name)
            }

            for(i in 0 until classVector.size) {
                if(result[i] == 0.0) continue
                result[i] = classVector[i] / result[i]
            }
            result.unit(EuclideanDistance)
            return result
        }
        return emptyVector
    }
    //#endregion

    //#region Update Ops
    private fun updateClouds() = clouds.forEach { (_, cloud) ->
        cloud.mapNotNull { dataCache[it]?.vector }.forEach { updateIntVec(it) }
    }

    private fun updateIntVec(vec: Vector) {
        val vecList = ArrayList<Double>(dictVec.dictionary.size)

        vecList.addAll(vec.data)
        for(i in vecList.size until dictVec.dictionary.size) {
            vecList.add(0.0)
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
    fun isClassified(dataRef: String) = dataRef !in unclassified
    fun unclassified() = unclassified.toMutableList()
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