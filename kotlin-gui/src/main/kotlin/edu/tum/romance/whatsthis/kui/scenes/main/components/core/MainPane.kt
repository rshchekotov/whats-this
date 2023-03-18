package edu.tum.romance.whatsthis.kui.scenes.main.components.core

import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import java.awt.Dimension
import javax.swing.JScrollPane

object MainPane: JScrollPane(MainModel.render, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    override fun setPreferredSize(preferredSize: Dimension?) {
        super.setPreferredSize(preferredSize)
        for(render in MainModel.renders.map { it.second }) {
            render.preferredSize = preferredSize
        }
    }
}