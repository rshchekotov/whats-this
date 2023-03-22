package edu.tum.romance.whatsthis.v1.nlp

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.v1.math.Distance
import org.apache.logging.log4j.LogManager
import java.util.*

@Suppress("unused", "EqualsOrHashCode")
object API {
    private var logger = LogManager.getLogger(API::class.java)
    internal val spaces = VectorSpaceManager
    internal val vectors = WordVectorManager
    internal val vocabulary = VocabularyManager

    /**
     * Create a sample in the API.
     *
     * @param sample TextData object containing the sample
     * @param space Name of the space to assign the sample to
     * or null to assign it to create a variable sample.
     */
    fun addSample(sample: TextData<*>, space: String? = null) {
        /* Register new Vocabulary */
        for((word, _) in sample.tokens) {
            if(word !in vocabulary) {
                vocabulary += word
            }
        }
        /* Create Text Vector */
        val vector = VocabularyManager.emptyVector()
        for((word, count) in sample.tokens) {
            vector[vocabulary[word]] = count
        }
        sample.vector = vector
        /* Grow remaining vectors to vocabulary size */
        for(ref in WordVectorManager.names()) {
            vectors[ref]!!.vector!!.growTo(vector.size())
        }
        /* Register new Vector */
        val ref: Int
        if(sample.name in WordVectorManager.names()) {
            vectors[sample.name] = sample
            ref = WordVectorManager.ref(sample.name)
        } else {
            ref = WordVectorManager.size()
            vectors += sample
        }
        /* Assign the Vector to a VectorSpace */
        when (space) {
            null -> VectorSpaceManager.unclassified(ref)
            in spaces -> spaces[space]!! += ref
            else -> {
                spaces += space
                spaces[space]!! += ref
            }
        }
        /* Recalculate Summaries & Significance */
        recalculateSignificance()
    }

    /**
     * Get a sample from the API.
     *
     * @param name Name of the sample to get
     *
     * @return TextData object containing the sample, if it exists.
     */
    fun getSample(name: String): TextData<*>? = vectors[name]

    /**
     * Rename a sample in the API.
     *
     * @param old Old name of the sample
     * @param new New name of the sample
     */
    fun renameSample(old: String, new: String) {
        if(old in vectors) {
            WordVectorManager.rename(old, new)
        }
    }

    /**
     * Delete a sample from the API.
     *
     * @param name Name of the sample to delete
     */
    fun deleteSample(name: String) {
        val ref = WordVectorManager.ref(name)
        if(ref == -1) {
            return
        }

        if(ref in VectorSpaceManager.unclassified()) {
            VectorSpaceManager.removeUnclassified(ref)
        }
        vectors -= vectors[name]!!
        VectorSpaceManager.deleteRef(ref)
        recalculateSignificance()
    }

    /**
     * Reassign a sample in the API.
     *
     * @param name Name of the sample to reassign
     * @param space Name of the space to assign the sample to
     * or null to assign it to create a variable sample.
     */
    fun resample(name: String, space: String? = null) {
        val sample = vectors[name]
        if(sample != null) {
            val ref = WordVectorManager.ref(name)
            if(space == null) {
                if(ref !in VectorSpaceManager.unclassified()) {
                    VectorSpaceManager.unclassified(ref)
                }
            } else {
                val spaceObj = spaces[space]
                if(spaceObj != null) {
                    spaceObj += ref
                } else {
                    spaces += space
                    spaces[space]!! += ref
                }
            }
        }
        logger.error("Sample $name does not exist! Please verify your front-end code.")
    }

    /**
     * Retrieve all vectors of the API, which are assigned
     * to some space.
     * Useful if you want to display unfiltered classified
     * vectors in a UI.
     *
     * @return List of vector names
     */
    fun classified(): List<String> {
        return WordVectorManager.names().filter { WordVectorManager.ref(it) !in VectorSpaceManager.unclassified() }
    }

    /**
     * Retrieve all vectors of the API, which are assigned
     * to a specific space or to no space at all.
     *
     * @param name Name of the space to retrieve vectors from
     * or null to retrieve all unclassified vectors
     * @return List of vector names
     */
    fun spaceVectors(name: String? = null): List<String> {
        return if(name != null) {
            spaces[name]?.let { space -> space.vectors().map { WordVectorManager.name(it) } } ?: emptyList()
        } else {
            VectorSpaceManager.unclassified().map { WordVectorManager.name(it) }
        }
    }

    /**
     * Retrieve all space names of the API as a list of strings.
     * Useful for displaying the available spaces in a GUI.
     *
     * @return List of space names
     */
    fun spaces(): List<String> {
        return VectorSpaceManager.spaces()
    }

    /**
     * Create or rename a space in the API.
     * This function is useful to create empty spaces
     * or to rename existing spaces and is thus reserved
     * for UI use.
     *
     * @param space Name of the space to create or rename
     * @param rename New name of the space, or null to keep the old name
     */
    fun alterSpace(space: String, rename: String? = null): Int {
        if(rename != null) {
            return if(space in spaces) {
                VectorSpaceManager.rename(space, rename)
                2
            } else {
                spaces += rename
                1
            }
        } else if(space !in spaces) {
            spaces += space
            return 1
        }
        return 0
    }

    /**
     * Delete a space from the API.
     *
     * @param space Name of the space to delete
     */
    fun deleteSpace(space: String) {
        if(space !in spaces) {
            return
        }
        for(ref in spaces[space]!!.vectors()) {
            VectorSpaceManager.unclassified(ref)
        }
        spaces -= space
        recalculateSignificance()
    }

    /**
     * Recalculate the significance of all spaces,
     * which is used to compute vector distances.
     */
    private fun recalculateSignificance(force: Boolean = false) {
        if(VectorSpaceManager.isEmpty()) {
            return
        }
        val summaries = VectorSpaceManager.spaces()
            .map { spaces[it]!!.name to spaces[it]!!.summary(Distance.Implementation, force) }
        val total = summaries.map { it.second }.reduce { a, b -> a + b }
        for((name, summary) in summaries) {
            val result = total.clone()
            for(i in 0 until result.size()) {
                if(result[i] == 0.0) {
                    continue
                }
                result[i] = summary[i] / result[i]
            }
            spaces[name] = result.unit()
        }
    }

    /**
     * Compute the distance between a sample and all spaces.
     *
     * @param value Name of the sample to compute the distance for
     *
     * @return List of distances between the sample and all spaces
     */
    fun distances(value: String): List<Double> {
        val data = vectors[value] ?: return emptyList()
        recalculateSignificance(true)
        val vector = data.vector!!.unit()
        val norms = VectorSpaceManager.spaces().map {
            val significance = VectorSpaceManager.significance(it)!!
            Distance.Implementation(vector, significance)
        }
        return norms
    }

    /**
     * Check whether the API contains any data, useful to check whether
     * a model can be safely imported without overwriting existing data.
     *
     * @return True if the API is empty, false otherwise
     */
    fun isEmpty(): Boolean {
        return VocabularyManager.isEmpty() && WordVectorManager.isEmpty() && VectorSpaceManager.isEmpty()
    }

    /**
     * Clear all data from the API.
     */
    fun clear() {
        VectorSpaceManager.clear()
        WordVectorManager.clear()
        VocabularyManager.clear()
    }

    override fun hashCode(): Int {
        return Objects.hash(vectors, spaces, vocabulary)
    }

    fun words(): List<String> {
        return VocabularyManager.words()
    }
}

/**
 * POJO representation of classified text-data objects..
 */
typealias NLPModel = MutableList<Pair<String?, List<() -> TextData<*>>>>