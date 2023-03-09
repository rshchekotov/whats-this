package edu.tum.romance.whatsthis.ui.panels.main.loader

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.Dimension
import javax.swing.*

object ModelImporter: JDialog(ClassificationFrame, "Loading Data", true) {
    val progress = JProgressBar(0, 100)
    private lateinit var task: ModelImportTask

    init {
        //#region Dialog Layout
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.layout = layout
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        val label = JLabel("Loading Data")
        label.font = ClassificationFrame.fonts[1]
        panel.add(label)
        panel.add(Box.createRigidArea(Dimension(0, 10)))
        progress.font = ClassificationFrame.fonts[0]
        val metrics = progress.getFontMetrics(progress.font)
        val example = "Loading ${"a".repeat(20)} from ${"b".repeat(16)}"
        progress.preferredSize = Dimension(metrics.stringWidth(example), (metrics.height * 1.5).toInt())
        progress.isStringPainted = true
        panel.add(progress)
        add(panel)
        pack()
        setLocationRelativeTo(ClassificationFrame)
        //#endregion
    }

    operator fun invoke(model: Map<String, List<Pair<String, () -> TextData<*>>>>) {
        progress.value = 0

        task = ModelImportTask(model, this)
        task.execute()

        isVisible = true
    }
}