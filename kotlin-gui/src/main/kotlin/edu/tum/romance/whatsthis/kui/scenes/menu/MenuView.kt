package edu.tum.romance.whatsthis.kui.scenes.menu

import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.components.StyledLabel
import edu.tum.romance.whatsthis.kui.scenes.View
import edu.tum.romance.whatsthis.kui.scenes.main.MainView
import edu.tum.romance.whatsthis.kui.scenes.main.menubar.FileMenu
import edu.tum.romance.whatsthis.kui.util.FontCache.HUGE
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.emptyBorder
import edu.tum.romance.whatsthis.kui.util.gridBagVSpace
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

@Suppress("unused")
object MenuView: View() {
    init {
        this.layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            weightx = 1.0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = GridBagConstraints.REMAINDER
        }

        add(StyledLabel(HUGE, "What's This?"), constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "New Model") { MainView.switch() }, constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "Load Model") { FileMenu.loadModel() }, constraints)
        emptyBorder(10)
    }
}