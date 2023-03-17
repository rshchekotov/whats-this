package edu.tum.romance.whatsthis.ui.views.main.components.main.dictionary

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.JList
import javax.swing.JScrollPane

object DictionaryView: JScrollPane(JList<String>(), VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), Loadable {
    val list = this.viewport.view as JList<*>

    init {
        list.font = ClassificationFrame.fonts[0]
        list.model = DictionaryListModel
        list.layoutOrientation = JList.HORIZONTAL_WRAP
        list.visibleRowCount = -1
        list.cellRenderer = DictionaryCellRenderer
    }

    override fun onLoad() {
        DictionaryListModel.onLoad()
        MainView.selectedData.observe(3) {
            list.repaint()
        }
    }

    override fun onUnload() {
        DictionaryListModel.onUnload()
        MainView.selectedData.stopObserving(3)
    }
}