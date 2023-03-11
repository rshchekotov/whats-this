package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.menu.ViewMenu
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextArea
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter

//#region Core Components
object MainViewComponent: JScrollPane(
    JTextArea(),
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    private val textArea = viewport.view as JTextArea
    private val tokenTable = JTable()
    private val dictionaryTable = JList<String>()

    private val scrollerWidth = verticalScrollBar.preferredSize.width
    private val preferredWidth
        get() = MainViewComponent.preferredSize.width - scrollerWidth

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

        tokenTable.fillsViewportHeight = true
        tokenTable.autoCreateRowSorter = true
        tokenTable.autoResizeMode = JTable.AUTO_RESIZE_OFF
        tokenTable.font = ClassificationFrame.fonts[0]
        tokenTable.rowHeight = 20
        tokenTable.model = DefaultTableModel(arrayOf<Array<String>>(), arrayOf("Word", "Vector"))

        dictionaryTable.font = ClassificationFrame.fonts[0]
        dictionaryTable.model = DefaultListModel()

        val wordColumn = tokenTable.getColumn("Word")
        val vectorColumn = tokenTable.getColumn("Vector")
        wordColumn.preferredWidth = 2 * preferredWidth / 3
        vectorColumn.preferredWidth = preferredWidth / 3

        val sorter = TableRowSorter(tokenTable.model)
        sorter.sortsOnUpdates = true
        tokenTable.rowSorter = sorter
    }

    fun update() {
        if(content.isNotBlank()) {
            if(ViewMenu.mode == ViewMenu.VEC) {
                val data = TextData(content, "")

                (tokenTable.model as DefaultTableModel).setDataVector(
                    data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
                    arrayOf("Word", "Vector"))

                val wordColumn = tokenTable.getColumn("Word")
                val vectorColumn = tokenTable.getColumn("Vector")
                wordColumn.preferredWidth = 2 * preferredWidth / 3
                vectorColumn.preferredWidth = preferredWidth / 3

                val sorter = tokenTable.rowSorter as TableRowSorter
                sorter.setComparator(wordColumn.modelIndex, Comparator.comparing(String::lowercase))
                sorter.setComparator(vectorColumn.modelIndex, Comparator.comparingInt(Int::toInt))
                sorter.toggleSortOrder(vectorColumn.modelIndex)
                sorter.toggleSortOrder(vectorColumn.modelIndex)

                viewport.view = tokenTable
            } else {
                viewport.view = textArea
            }
        }
    }

    fun isBlank(): Boolean = textArea.text.isBlank()
}