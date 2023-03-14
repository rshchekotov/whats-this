package edu.tum.romance.whatsthis.io.model

interface SerializableData<T> {
    fun serialize(): String
    fun deserialize(data: String)
}