package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import edu.tum.romance.whatsthis.kui.components.Loadable
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import java.awt.Component

interface MainPaneRender: Loadable {
    @Suppress("unused")
    fun switch() {
        MainModel.render.onUnload()
        MainModel.render = this
        MainModel.render.onLoad()
        getComponent().revalidate()
        MainPane.viewport.view = getComponent()
        MainPane.revalidate()
        MainPane.repaint()
    }

    fun getComponent(): Component

    override fun onLoad() {}
    override fun onUnload() {}
}