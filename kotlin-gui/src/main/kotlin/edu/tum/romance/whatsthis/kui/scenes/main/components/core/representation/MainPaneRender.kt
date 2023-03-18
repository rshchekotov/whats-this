package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import javax.swing.JPanel

abstract class MainPaneRender: JPanel() {
    @Suppress("unused")
    fun switch() {
        MainModel.render = this
        MainPane.viewport.view = this
    }
}