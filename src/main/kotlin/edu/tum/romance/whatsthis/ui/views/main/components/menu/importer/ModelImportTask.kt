package edu.tum.romance.whatsthis.ui.views.main.components.menu.importer

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.observer.trigger
import java.awt.Cursor
import java.awt.Toolkit
import javax.swing.SwingWorker

class ModelImportTask(
    private val model: List<Pair<String?, List<() -> TextData<*>>>>
): SwingWorker<Unit, Unit>() {
    private val maxProgress = model.map { it.second }.sumOf { it.size }

    override fun doInBackground() {
        var items = 0
        Thread {
            MainView.progressUpdate.value = "Initializing..." to 0.0
        }.start()
        API.clear()
        ClassificationFrame.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        Toolkit.getDefaultToolkit().beep()
        for((className, samples) in model) {
            for(sample in samples) {
                val data = sample()
                val sampleName = data.name
                API.addSample(data, className)
                items++
                Thread {
                    MainView.progressUpdate.value = "Loading $sampleName from $className" to 100 * items.toDouble() / maxProgress
                }.start()
            }
        }
    }

    override fun done() {
        ClassificationFrame.cursor = Cursor.getDefaultCursor()
        MainView.progressUpdate.value = "Done." to -1.0
        MainView.dataUpdate.trigger()
        MainView.spaceUpdate.trigger()
    }
}