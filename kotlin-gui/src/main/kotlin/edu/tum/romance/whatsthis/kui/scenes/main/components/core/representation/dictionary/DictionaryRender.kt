package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.dictionary

import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.MainPaneRender
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import javax.swing.JList

object DictionaryRender: JList<String>(), MainPaneRender {
    init {
        this.font = comfortaa(SMALL)
        this.model = DictionaryModel
        this.layoutOrientation = HORIZONTAL_WRAP
        this.visibleRowCount = -1
        this.cellRenderer = DictionaryCellRenderer
    }

    override fun onLoad() {
        this.fixedCellWidth = MainPane.viewport.width / 2
        this.fixedCellHeight = MEDIUM
    }

    override fun getComponent() = this
}