package edu.tum.romance.whatsthis.io.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import edu.tum.romance.whatsthis.io.data.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.nlp.NLPModel
import edu.tum.romance.whatsthis.ui.views.main.components.menu.importer.SourceImportTask
import java.io.File

interface SourceFormat {
    val header: String
    val file: File
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

        fun fromFile(file: File): SourceFormat {
            return when {
                file.absolutePath.endsWith(YAML_EXT) -> YAML(file)
                file.absolutePath.endsWith(DSL_EXT) -> DSL(file)
                else -> YAML(file)
            }
        }

        class YAML(override val file: File): SourceFormat {
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
                val data = DataFormat()
                data.spaces.addAll(API.spaces())
                for(space in API.spaces()) {
                    data.core[space] = mutableListOf()
                    for(vecRef in API.spaceVectors(space)) {
                        val vector = API.vectors[vecRef]
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
                    val vector = API.vectors[vecRef]
                    if(vector != null) {
                        val coreData = CoreData()
                        coreData.name = vector.name
                        coreData.source = vector.sourceID
                        coreData.data = vector.toString()
                        data.variable.add(coreData)
                    }
                }

                val out = if(!file.absolutePath.endsWith(YAML_EXT))
                    File(file.absolutePath + YAML_EXT)
                else file

                val mapper = ObjectMapper(YAMLFactory())
                val text = mapper.writeValueAsString(data)
                out.writeText("$header\n\n$text")
            }
            override fun read(): NLPModel {
                val mapper = ObjectMapper(YAMLFactory())
                val data = mapper.readValue(file, DataFormat::class.java)

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

        class DSL(override val file: File): SourceFormat {
            override val header = "# What's This Sources (DSL) v1.0.0"
            override fun write() {
                val contents = buildString {
                    for(space in API.spaces()) {
                        val safeSpace = space.replace("\"", "\\\"")
                        appendLine("space(\"$safeSpace\")")
                    }
                    for(space in API.spaces()) {
                        val safeSpace = space.replace("\"", "\\\"")
                        for(vector in API.spaceVectors(space)) {
                            val data = API.vectors[vector]
                            if(data != null) {
                                val safeName = data.name.replace("\"", "\\\"")
                                val safeData = data.toString().replace("\"", "\\\"")
                                appendLine("core(\"$safeName\", \"${data.sourceID}\", \"$safeData\", \"$safeSpace\")")
                            }
                        }
                    }
                    for(vector in API.spaceVectors()) {
                        val data = API.vectors[vector]
                        if(data != null) {
                            val safeName = data.name.replace("\"", "\\\"")
                            val safeData = data.toString().replace("\"", "\\\"")
                            appendLine("variable(\"$safeName\", \"${data.sourceID}\", \"$safeData\")")
                        }
                    }
                }
                (if(!file.absolutePath.endsWith(DSL_EXT)) {
                    File(file.absolutePath + DSL_EXT)
                } else file).writeText(contents)
            }

            override fun read(): NLPModel {
                return file.readLines().mapNotNull {
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