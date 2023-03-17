package edu.tum.romance.whatsthis.ui.views.main.components.source

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.JComboBox

object SourceSelector: JComboBox<String>() {
    init {
        this.font = ClassificationFrame.fonts[0]
        this.toolTipText = "Select a source to load data from"
        MainView.sources.map { it.first }.forEach { this.addItem(it) }
        this.addActionListener { MainView.selectedSourceType.value = this.selectedIndex }
    }
}