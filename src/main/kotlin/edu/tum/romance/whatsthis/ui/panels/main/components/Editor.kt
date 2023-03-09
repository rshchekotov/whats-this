package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.menu.ViewMenu
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextArea
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter

//#region Core Components
object Editor: JScrollPane(
    JTextArea(),
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    val textArea = viewport.view as JTextArea
    val table = JTable()

    val scrollerWidth = verticalScrollBar.preferredSize.width
    val preferredWidth
        get() = Editor.preferredSize.width - scrollerWidth

    var content: String
        get() = textArea.text
        set(value) {
            textArea.text = value
            update()
        }

    init {
        textArea.isEditable = true
        textArea.lineWrap = true
        textArea.font = ClassificationFrame.fonts[0]

        table.fillsViewportHeight = true
        table.autoCreateRowSorter = true
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
        table.font = ClassificationFrame.fonts[0]
        table.rowHeight = 20
        table.model = DefaultTableModel(arrayOf<Array<String>>(), arrayOf("Word", "Vector"))

        val wordColumn = table.getColumn("Word")
        val vectorColumn = table.getColumn("Vector")
        wordColumn.preferredWidth = 2 * preferredWidth / 3
        vectorColumn.preferredWidth = preferredWidth / 3

        val sorter = TableRowSorter(table.model)
        sorter.sortsOnUpdates = true
        table.rowSorter = sorter
    }

    fun update() {
        if(content.isNotBlank()) {
            if(ViewMenu.mode == ViewMenu.VEC) {
                val data = TextData(content)

                (table.model as DefaultTableModel).setDataVector(
                    data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
                    arrayOf("Word", "Vector"))

                val wordColumn = table.getColumn("Word")
                val vectorColumn = table.getColumn("Vector")
                wordColumn.preferredWidth = 2 * preferredWidth / 3
                vectorColumn.preferredWidth = preferredWidth / 3

                val sorter = table.rowSorter as TableRowSorter
                sorter.setComparator(wordColumn.modelIndex, Comparator.comparing(String::lowercase))
                sorter.setComparator(vectorColumn.modelIndex, Comparator.comparingInt(Int::toInt))
                sorter.toggleSortOrder(vectorColumn.modelIndex)
                sorter.toggleSortOrder(vectorColumn.modelIndex)

                viewport.view = table
            } else {
                viewport.view = textArea
            }
        }
    }

    fun isBlank(): Boolean = textArea.text.isBlank()
}