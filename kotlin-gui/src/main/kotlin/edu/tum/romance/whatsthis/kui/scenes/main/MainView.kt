package edu.tum.romance.whatsthis.kui.scenes.main

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.scenes.View
import edu.tum.romance.whatsthis.kui.scenes.main.components.buttons.MainButtonPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.SamplePane
import edu.tum.romance.whatsthis.kui.scenes.main.menubar.TopBar
import edu.tum.romance.whatsthis.kui.util.minus
import edu.tum.romance.whatsthis.kui.util.times
import java.awt.Dimension
import javax.swing.SpringLayout
import javax.swing.SpringLayout.*

object MainView: View() {
    private const val padding = 5
    // Hacky Hardcoded Value
    private const val tabHeight = 32

    init {
        val layout = SpringLayout()
        this.layout = layout
        this.preferredSize = Dimension(Main.frameWidth, Main.frameHeight)

        val contentSize = this.preferredSize - (10 to 10)

        this.add(MainPane)
        MainPane.preferredSize = contentSize * (1.0 to 0.7)
        layout.putConstraint(WEST, MainPane, padding, WEST, this)
        layout.putConstraint(NORTH, MainPane, padding, NORTH, this)
        layout.putConstraint(EAST, MainPane, -padding, EAST, this)

        this.add(SamplePane)
        SamplePane.preferredSize = contentSize * (0.7 to 0.3)
        layout.putConstraint(WEST, SamplePane, 0, WEST, MainPane)
        layout.putConstraint(NORTH, SamplePane, padding, SOUTH, MainPane)
        layout.putConstraint(SOUTH, SamplePane, -padding, SOUTH, this)

        this.add(MainButtonPane)
        MainButtonPane.preferredSize = contentSize * (0.3 to 0.3)
        layout.putConstraint(WEST, MainButtonPane, padding, EAST, SamplePane)
        layout.putConstraint(NORTH, MainButtonPane, tabHeight, NORTH, SamplePane)
        layout.putConstraint(EAST, MainButtonPane, -padding, EAST, this)
        layout.putConstraint(SOUTH, MainButtonPane, 0, SOUTH, SamplePane)
    }

    override fun onLoad() {
        Main.jMenuBar = TopBar
    }

    override fun onUnload() {
        Main.jMenuBar = null
    }
}