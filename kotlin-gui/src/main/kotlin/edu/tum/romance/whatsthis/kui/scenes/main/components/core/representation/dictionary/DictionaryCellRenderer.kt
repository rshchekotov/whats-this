package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.dictionary

import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import java.awt.Color
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.ListCellRenderer

object DictionaryCellRenderer: ListCellRenderer<String> {
    private val default = DefaultListCellRenderer()
    private const val gradientStart = 0x348ac7
    private const val gradientEnd = 0xfc354c

    override fun getListCellRendererComponent(
        list: JList<out String>?,
        value: String?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val result = default.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus
        )

        val data = MainModel.data ?: return result

        val tokens = data.tokens
        val max = tokens.maxBy { it.second }.second
        val pair = tokens.find { it.first == value } ?: (value to 0.0)
        result.background = Color(gradient(
            pair.second.toDouble(),
            0.0, max.toDouble(),
            gradientStart, gradientEnd))
        return result
    }

    @Suppress("SameParameterValue")
    private fun gradient(
        value: Double,
        min: Double,
        max: Double,
        minColor: Int,
        maxColor: Int
    ): Int {
        val range = max - min
        val valueInRange = value - min
        val ratio = valueInRange / range
        val red = (minColor shr 16 and 0xFF) + ((maxColor shr 16 and 0xFF) - (minColor shr 16 and 0xFF)) * ratio
        val green = (minColor shr 8 and 0xFF) + ((maxColor shr 8 and 0xFF) - (minColor shr 8 and 0xFF)) * ratio
        val blue = (minColor and 0xFF) + ((maxColor and 0xFF) - (minColor and 0xFF)) * ratio
        return (red.toInt() shl 16) + (green.toInt() shl 8) + blue.toInt()
    }
}