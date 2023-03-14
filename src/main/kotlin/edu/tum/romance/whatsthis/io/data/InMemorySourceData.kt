package edu.tum.romance.whatsthis.io.data

class InMemorySourceData(override var source: String, override var name: String): TextData<String>() {
    override val sourceID: String = "memory"
    override var text: String = source
    override var titleSuggestion: String = ""
}