package edu.tum.romance.whatsthis.kui.scenes.main.components.samples

import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.FixedPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.VariablePane
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Dimension
import javax.swing.JTabbedPane

object SamplePane: JTabbedPane(TOP) {
    init {
        font = comfortaa(SMALL)
        addTab("Classified Samples", FixedPane)
        addTab("Unclassified Samples", VariablePane)
        addChangeListener {
            MainModel.variable = selectedIndex == 1
        }
    }

    override fun setPreferredSize(preferredSize: Dimension) {
        super.setPreferredSize(preferredSize)
        FixedPane.preferredSize = preferredSize
        VariablePane.preferredSize = preferredSize
    }
}