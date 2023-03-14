package edu.tum.romance.whatsthis.io.data

class InMemorySourceData(override var source: String, override var name: String): TextData<String>() {
    override var text: String = source
    override var titleSuggestion: String = ""
    override fun deserialize(data: String) = error("InMemorySourceData cannot be deserialized!")
    override fun serialize() = error("InMemorySourceData cannot be serialized!")
}