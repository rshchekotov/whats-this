package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.Vector

@Suppress("unused")
object Monitor {
    private val dataCache = mutableMapOf<String, TextData<*>>()
    private val clouds: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val unclassified = mutableListOf<String>()
    private val dictVec = WordVec()
    private val summaryCache = mutableMapOf<Int, Vector>()
    private val significanceCache = mutableMapOf<Int, Vector>()

    val emptyVector
        get() = Vector(dictVec.dictionary.size)
    val vocabulary
        get() = dictVec.dictionary.toSet()

    //#region CRUD Ops
    /**
     * Adds a new data with its name to the cloud(by name as String)
     * @param name The name of the TextData
     * @param data The sample data
     * @param cloud The cloud to assign the sample to
     */
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

    /**
     * Assigns a data to a cloud, removing it from the previous cloud
     * @param dataRef The name of the data
     * @param cloud The cloud to assign the data to
     */
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

    /**
     * Revoke a data from a cloud and classify it as "unclassified"
     * @param dataRef The name of the data
     * @param cloud The cloud to revoke the data from
     */
    fun revoke(dataRef: String, cloud: String): Monitor {
        clouds[cloud]?.remove(dataRef)
        unclassified += dataRef
        updateClouds()
        return this
    }

    /**
     * Clears all data from the monitor
     */
    fun clear() {
        //TODO: Clear all data caches
        clouds.clear()
        dataCache.clear()
        dictVec.clear()
        unclassified.clear()
    }

    fun isEmpty(): Boolean = clouds.isEmpty() && dataCache.isEmpty() && dictVec.dictionary.isEmpty()
    //#endregion

    //#region Math Stuff
    //Finds the closest cloud to a data point
    /*
    private fun findClosestCloud(data: TextData<*>, distance: Distance = EuclideanDistance): String {
        dictVec.createVec(data)
        return clouds.minByOrNull { cloud ->
            if(cloud.value.isEmpty()) return@minByOrNull Double.MAX_VALUE
            (cloud.value.map { dataCache[it] }.minByOrNull { distance(it?.vector!!, data.vector!!) }?.vector?.let {
                distance(it, data.vector!!)
            } ?: Double.MAX_VALUE)
        }?.key ?: "default"
    }
    */

    /**
     * Summarizes the cloud by calculating the average vector of all data in the cloud
     * @param cloud The cloud to summarize
     */
    fun summary(cloud: String): Vector {
        if(cloud in Monitor && Monitor[cloud]!!.isNotEmpty()) {
            val hash = clouds[cloud]!!.hashCode()
            if(hash in summaryCache) return summaryCache[hash]!!

            val vec = emptyVector
            clouds[cloud]!!.forEach { vec += dataCache[it]!!.vector!! }
            vec.unit(EuclideanDistance)
            summaryCache[hash] = vec
            return vec
        }
        return emptyVector
    }

    /**
     * Calculates the significance of the cloud by dividing the average vector of the cloud by the average vector of all clouds
     * @param cloud The cloud to calculate the significance of
     */
    fun significance(cloud: String): Vector {
        if(cloud in Monitor && Monitor[cloud]!!.isNotEmpty()) {
            val classVector = summary(cloud)

            var result: Vector = emptyVector

            val hash = clouds.hashCode()
            if (hash in significanceCache) {
                result = significanceCache[hash]!!.clone()
            } else {
                for ((name, _) in clouds) {
                    result += summary(name)
                }
                significanceCache[hash] = result.clone()
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