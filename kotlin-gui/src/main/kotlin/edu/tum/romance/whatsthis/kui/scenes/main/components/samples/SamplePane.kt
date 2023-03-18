package edu.tum.romance.whatsthis.kui.scenes.main.components.samples

import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.FixedPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.VariablePane
import javax.swing.JTabbedPane

object SamplePane: JTabbedPane() {
    init {
        addTab("Classified Samples", FixedPane)
        addTab("Unclassified Samples", VariablePane)
    }
}