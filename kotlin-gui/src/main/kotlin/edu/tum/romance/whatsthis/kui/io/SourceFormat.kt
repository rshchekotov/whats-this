package edu.tum.romance.whatsthis.kui.io

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import edu.tum.romance.whatsthis.data.TextData
import edu.tum.romance.whatsthis.kui.popup.importer.SourceImportTask
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.nlp.NLPModel
import java.io.*

interface SourceFormat {
    val header: String
    val input: () -> InputStream?
    val output: () -> OutputStream?

    fun write()
    fun read(): NLPModel
    fun load() {
        val model = read()
        SourceImportTask(model).execute()
    }

    @Suppress("unused")
    fun loadHeadless() {
        val model = read()
        for((space, samples) in model) {
            for(data in samples) {
                API.addSample(data(), space)
            }
        }
    }

    companion object {
        const val YAML_EXT = ".wts.yaml"
        const val DSL_EXT = ".wts"

        fun fromFile(file: File, ext: String = "YAML"): SourceFormat {
            if(!file.exists()) {
                file.createNewFile()
            }
            val construct = when(ext) {
                "DSL" -> ::DSL
                else -> ::YAML
            }
            val input = file::inputStream
            val output = file::outputStream
            return construct(input, output)
        }

        fun fromIOStream(input: InputStream?, output: OutputStream?, ext: String = "YAML"): SourceFormat {
            return if(ext == "YAML") YAML({ input }, { output })
            else DSL({ input }, { output })
        }

        class YAML(
            override val input: () -> InputStream?,
            override val output: () -> OutputStream?
        ): SourceFormat {
            internal class DataFormat {
                var spaces: MutableList<String> = mutableListOf()
                var core: MutableMap<String, MutableList<CoreData>> = mutableMapOf()
                var variable: MutableList<CoreData> = mutableListOf()
            }

            internal class CoreData {
                var name: String = ""
                var source: String = ""
                var data: String = ""
            }

            override val header = "# What's This Sources (YAML) v1.0.0"
            override fun write() {
                val out = output() ?: error("Operation not supported on a Read-Only Source.")

                val data = DataFormat()
                data.spaces.addAll(API.spaces())
                for(space in API.spaces()) {
                    data.core[space] = mutableListOf()
                    for(vecRef in API.spaceVectors(space)) {
                        val vector = API.getSample(vecRef)
                        if(vector != null) {
                            val coreData = CoreData()
                            coreData.name = vector.name
                            coreData.source = vector.sourceID
                            coreData.data = vector.toString()
                            data.core[space]!!.add(coreData)
                        }
                    }
                }

                for(vecRef in API.spaceVectors()) {
                    val vector = API.getSample(vecRef)
                    if(vector != null) {
                        val coreData = CoreData()
                        coreData.name = vector.name
                        coreData.source = vector.sourceID
                        coreData.data = vector.toString()
                        data.variable.add(coreData)
                    }
                }

                val mapper = ObjectMapper(YAMLFactory())
                val text = mapper.writeValueAsString(data)
                out.write(("$header\n\n$text").toByteArray())
            }
            override fun read(): NLPModel {
                val input = input() ?: error("Operation not supported on a Write-Only Source.")

                val mapper = ObjectMapper(YAMLFactory())
                val data = mapper.readValue(input, DataFormat::class.java)

                val model: NLPModel = mutableListOf()
                for((space, samples) in data.core) {
                    model.add(space to samples.map { sample ->
                        { TextData(sample.source, sample.name, sample.data) }
                    })
                }
                model.add(null to data.variable.map { sample ->
                    { TextData(sample.source, sample.name, sample.data) }
                })
                return model
            }
        }

        class DSL(
            override val input: () -> InputStream?,
            override val output: () -> OutputStream?
        ): SourceFormat {
            override val header = "# What's This Sources (DSL) v1.0.0"
            override fun write() {
                val output = output() ?: error("Operation not supported on a Read-Only Source.")

                val contents = buildString {
                    appendLine(header).appendLine()
                    for(space in API.spaces()) {
                        val safeSpace = space.replace("\"", "\\\"")
                        appendLine("space(\"$safeSpace\")")
                    }
                    for(space in API.spaces()) {
                        val safeSpace = space.replace("\"", "\\\"")
                        for(vector in API.spaceVectors(space)) {
                            val data = API.getSample(vector)
                            if(data != null) {
                                val safeName = data.name.replace("\"", "\\\"")
                                val safeData = data.toString().replace("\"", "\\\"")
                                appendLine("core(\"$safeName\", \"${data.sourceID}\", \"$safeData\", \"$safeSpace\")")
                            }
                        }
                    }
                    for(vector in API.spaceVectors()) {
                        val data = API.getSample(vector)
                        if(data != null) {
                            val safeName = data.name.replace("\"", "\\\"")
                            val safeData = data.toString().replace("\"", "\\\"")
                            appendLine("variable(\"$safeName\", \"${data.sourceID}\", \"$safeData\")")
                        }
                    }
                }
                output.write(contents.toByteArray())
            }

            override fun read(): NLPModel {
                val input = input() ?: error("Operation not supported on a Write-Only Source.")

                val reader = BufferedReader(InputStreamReader(input))
                val lines = reader.readLines()
                return lines.asSequence().filter {
                    it.isNotBlank() || it.startsWith('#')
                }.mapNotNull {
                    val coreMatch = coreRegEx.matchEntire(it)
                    if(coreMatch != null) {
                        val (name, sourceID, source, space) = coreMatch.destructured
                        val safeName = name.replace("\\\"", "\"")
                        val safeSource = source.replace("\\\"", "\"")
                        val safeSpace = space.replace("\\\"", "\"")
                        return@mapNotNull safeSpace to { TextData(sourceID, safeName, safeSource) }
                    }

                    val variableMatch = variableRegEx.matchEntire(it)
                    if(variableMatch != null) {
                        val (name, sourceID, source) = variableMatch.destructured
                        val safeName = name.replace("\\\"", "\"")
                        val safeSource = source.replace("\\\"", "\"")
                        return@mapNotNull null to { TextData(sourceID, safeName, safeSource) }
                    }

                    return@mapNotNull null
                }.groupBy({ it.first }, { it.second }).toList().toMutableList()
            }

            companion object {
                val coreRegEx = Regex("^core\\(\"(.+?)(?<!\\\\)\", \"(.+?)(?<!\\\\)\", \"(.+?)(?<!\\\\)\", \"(.+?)(?<!\\\\)\"\\)\$")
                val variableRegEx = Regex("^variable\\(\"(.+?)(?<!\\\\)\", \"(.+?)(?<!\\\\)\", \"(.+?)(?<!\\\\)\"\\)\$")
            }
        }
    }
}