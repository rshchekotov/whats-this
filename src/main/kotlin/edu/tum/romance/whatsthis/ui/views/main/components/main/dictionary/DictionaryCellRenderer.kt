package edu.tum.romance.whatsthis.ui.views.main.components.main.dictionary

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.Color
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.ListCellRenderer

object DictionaryCellRenderer: ListCellRenderer<String> {
    override fun getListCellRendererComponent(
        list: JList<out String>?,
        value: String?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if(list != DictionaryView.list) {
            return defaultRenderer(list, value, index, isSelected, cellHasFocus)
        }

        val content = MainView.content.value
        if(content != null) {
            return wordRenderer(list, value, index, isSelected, cellHasFocus) { word ->
                content.tokens.map { it.first }.contains(word)
            }
        } else {
            val selection = MainView.selectedData.value
            if(selection != -1) {
                val data = API.vectors[selection]
                if(data == null) {
                    visualError("Selected data is not in the vector space")
                    return defaultRenderer(list, value, index, isSelected, cellHasFocus)
                }
                return wordRenderer(list, value, index, isSelected, cellHasFocus) { word ->
                    data.tokens.map { it.first }.contains(word)
                }
            } else {
                return defaultRenderer(list, value, index, isSelected, cellHasFocus)
            }
        }
    }

    private fun wordRenderer(
        list: JList<out String>?,
        value: String?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean,
        inData: (String) -> Boolean
    ): Component {
        val label = defaultRenderer(list, value, index, isSelected, cellHasFocus)
        if(value != null && inData(value)) {
            label.background = Color(0xadd8e6)
        }
        return label
    }

    private fun defaultRenderer(list: JList<out String>?, value: String?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        return DefaultListCellRenderer()
            .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
    }
}