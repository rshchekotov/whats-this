package edu.tum.romance.whatsthis.ui.views.main.components.menu.importer

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.components.StyledLabel
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.Dimension
import javax.swing.*

object ModelImporter: JDialog(ClassificationFrame, "Loading Data", true) {
    private val progress = JProgressBar(0, 100)

    /**
     * Serves no purpose other than to 'load' the class
     * and make it show as 'used'.
     *
     * All the logic happens within the observer callback.
     */
    fun classLoad() {}

    init {
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.layout = layout
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        val label = StyledLabel(1, "Importing Data...")
        panel.add(label)
        panel.add(Box.createRigidArea(Dimension(0, 10)))
        progress.font = ClassificationFrame.fonts[0]
        val metrics = progress.getFontMetrics(progress.font)
        val example = "Loading ${"a".repeat(20)} from ${"b".repeat(16)}"
        progress.preferredSize = Dimension(metrics.stringWidth(example), (metrics.height * 1.5).toInt())
        progress.isStringPainted = true
        progress.string = MainView.progressUpdate.value.first
        panel.add(progress)
        this.add(panel)
        this.pack()
        this.setLocationRelativeTo(ClassificationFrame)

        MainView.progressUpdate.observe(1) {
            val (old, new) = it
            val (_, oldState) = old
            val (text, state) = new

            progress.value = state.toInt()
            progress.string = text

            if(oldState == -1.0) {
                isVisible = true
            }
            if(state == -1.0) {
                dispose()
            }
        }
    }
}