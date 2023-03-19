package edu.tum.romance.whatsthis.kui.popup.importer

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.components.StyledLabel
import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.progress.ProgressUpdateEvent
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Dimension
import javax.swing.*

@Suppress("unused") /* Intentionally */
object SourceImporter: JDialog(Main, "Loading Data", true) {
    private val progress = JProgressBar(0, 100)

    init {
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.layout = layout
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        val label = StyledLabel(MEDIUM, "Importing data...")
        panel.add(label)
        panel.add(Box.createRigidArea(Dimension(0, 10)))

        progress.font = comfortaa(SMALL)
        val metrics = progress.getFontMetrics(progress.font)
        val example = "Loading ${"a".repeat(20)} from ${"b".repeat(16)}"
        progress.preferredSize = Dimension(metrics.stringWidth(example), progress.preferredSize.height * 4)
        progress.isStringPainted = true
        progress.string = "Inactive"
        panel.add(progress)

        add(panel)
        pack()
        setLocationRelativeTo(Main)
    }

    @EventHandler
    fun onProgressUpdate(event: ProgressUpdateEvent) {
        if(event.percentage == 0.0) {
            isVisible = true
        }

        if(event.percentage == -1.0) {
            dispose()
        } else {
            progress.value = (event.percentage * 100).toInt()
            progress.string = event.comment
        }
    }
}