package edu.tum.romance.whatsthis.io.model

import edu.tum.romance.whatsthis.io.data.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.views.main.components.menu.importer.SourceImportTask
import java.io.File
import java.lang.StringBuilder

object IO {
    private const val SOURCES_EXTENSION = "wts"
    private const val SOURCES_HEADER_INDICATOR = "#"
    private const val SOURCES_SPACE_INDICATOR = ">"
    private const val SOURCES_VECTOR_INDICATOR = "@"
    private const val SOURCES_COMMENT_INDICATOR = "%"
    private const val SOURCE_HEADER_PHASE = 0
    private const val SOURCE_SPACE_PHASE = 1
    private const val SOURCE_VECTOR_PHASE = 2

    private val vectorRegex = Regex("@(.*) ((?:web|file):[^:]+?:.+)")

    fun exportAsModel(file: File) {
        TODO(file.absolutePath)
    }

    fun importFromModel(file: File) {
        TODO(file.absolutePath)
    }

    fun exportAsSources(file: File) {
        // Write to file
        val text = buildString {
            appendLine("$SOURCES_COMMENT_INDICATOR What's This Sources").blank()
            // Spaces
            appendLine("$SOURCES_HEADER_INDICATOR Spaces")
            for(space in API.spaces()) {
                appendLine("$SOURCES_SPACE_INDICATOR ${space.replace("\n", "<br/>")}")
            }
            blank()
            // Vectors
            appendLine("$SOURCES_HEADER_INDICATOR Vectors")
            for(space in API.spaces()) {
                for(vector in API.spaceVectors(space)) {
                    val data = API.vectors[vector]
                    if(data != null) {
                        appendLine("$SOURCES_VECTOR_INDICATOR$space ${data.serialize().replace("\n", "<br/>")}")
                    }
                }
            }
            for(vector in API.spaceVectors(null)) {
                val data = API.vectors[vector]
                if(data != null) {
                    appendLine("$SOURCES_VECTOR_INDICATOR ${data.serialize().replace("\n", "<br/>")}")
                }
            }
        }

        (if(file.extension != SOURCES_EXTENSION)
            File(file.absolutePath + ".$SOURCES_EXTENSION") else file).writeText(text)
    }

    fun importFromSources(file: File) {
        if(file.extension != SOURCES_EXTENSION) {
            error("The file ${file.absolutePath} is not a 'What's This Sources'-file!")
        }
        API.clear()
        val lines = file.readLines()
        if(lines[0] != "$SOURCES_COMMENT_INDICATOR What's This Sources") {
            error("The file ${file.absolutePath} is not a 'What's This Sources'-file!")
        }

        var phase = SOURCE_HEADER_PHASE
        val spaces = mutableListOf<String>()
        val rawVectors = mutableListOf<Pair<String?, () -> TextData<*>>>()
        for(line in lines) {
            if(phase == SOURCE_HEADER_PHASE) {
                if(parseComment(line)) continue
                phase = parseHeaders(line, file, phase)
                continue
            }

            if(phase == SOURCE_SPACE_PHASE) {
                if(parseComment(line)) continue
                phase = parseHeaders(line, file, phase)
                if(phase == SOURCE_VECTOR_PHASE) continue
                parseSpaces(line)?.let { spaces.add(it) }
                continue
            }

            if(phase == SOURCE_VECTOR_PHASE) {
                if(parseComment(line)) continue
                val parsed = parseVectors(line)
                if(parsed != null) {
                    if(parsed.first != null && parsed.first !in spaces) {
                        error("The space '${parsed.first}' in file ${file.absolutePath} is not defined!")
                    }
                    rawVectors.add(parsed)
                }
            }
        }
        val vectors = rawVectors.groupBy({ it.first }, { it.second }).toList()

        SourceImportTask(vectors).execute()
    }

    private fun parseHeaders(line: String, file: File, phase: Int): Int {
        if(line.startsWith(SOURCES_HEADER_INDICATOR)) {
            return when(val header = line.substring(2)) {
                "Spaces" -> 1
                "Vectors" -> 2
                else -> error("Unknown header '$header' in file ${file.absolutePath}!")
            }
        }
        return phase
    }

    private fun parseComment(line: String): Boolean {
        return line.startsWith(SOURCES_COMMENT_INDICATOR)
    }

    private fun parseSpaces(line: String): String? {
        if(line.startsWith(SOURCES_SPACE_INDICATOR)) {
            return line.substring(2).trim()
        }
        return null
    }

    private fun parseVectors(line: String): Pair<String?, () -> TextData<*>>? {
        if(line.startsWith(SOURCES_VECTOR_INDICATOR)) {
            val match = vectorRegex.find(line)
            if(match != null) {
                val space = match.groupValues[1].trim()
                val vector = match.groupValues[2]
                return if(space.isNotBlank()) {
                    space to { TextData.deserialize(vector) }
                } else {
                    null to { TextData.deserialize(vector) }
                }
            }
        }
        return null
    }

    private fun StringBuilder.blank() {
        appendLine()
    }
}