package edu.tum.romance.whatsthis.ui.panels.main.loader

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.components.ClassList
import edu.tum.romance.whatsthis.ui.panels.main.components.SampleList
import java.awt.Cursor
import java.awt.Toolkit
import javax.swing.JDialog
import javax.swing.SwingWorker

class ModelImportTask(
    private val model: Map<String, List<Pair<String, () -> TextData<*>>>>,
    private val dialog: JDialog
):
    SwingWorker<Unit, Unit>() {
    private val maxProgress = model.values.sumOf { it.size }
    override fun doInBackground() {
        var items = 0
        progress = 0
        Monitor.clear()
        ClassList.update()
        SampleList.update()
        ClassificationFrame.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        Toolkit.getDefaultToolkit().beep()
        for((className, samples) in model) {
            for((sampleName, data) in samples) {
                Monitor.add(sampleName, data(), className)
                items++
                progress = (100.0 * items / maxProgress).toInt()

                ModelImporter.progress.value = progress
                ModelImporter.progress.string = "Loading $sampleName from $className"
            }
        }
    }

    override fun done() {
        ClassificationFrame.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
        ClassList.update()
        SampleList.update()
        dialog.dispose()
    }
}