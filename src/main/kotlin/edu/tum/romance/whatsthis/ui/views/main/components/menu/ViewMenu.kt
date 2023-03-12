package edu.tum.romance.whatsthis.ui.views.main.components.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem

object ViewMenu: JMenu("View") {
    init {
        font = ClassificationFrame.fonts[0]
        MainView.dataViews.forEachIndexed { index, name ->
            val item = JMenuItem(name.first)
            item.mnemonic = KeyEvent.VK_1 + index
            item.font = ClassificationFrame.fonts[0]
            item.addActionListener { MainView.selectedDataViewType.value = index }
            add(item)
        }
    }
}