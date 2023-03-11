package edu.tum.romance.whatsthis.io

class InMemorySourceData(override val source: String, override val name: String): TextData<String>() {
    override var text: String = source
    override var titleSuggestion: String = ""
}