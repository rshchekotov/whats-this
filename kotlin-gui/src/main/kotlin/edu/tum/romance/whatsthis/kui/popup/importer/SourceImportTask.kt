package edu.tum.romance.whatsthis.kui.popup.importer

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ProgressUpdateEvent
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.nlp.NLPModel
import java.awt.Cursor
import java.awt.Toolkit
import javax.swing.SwingWorker

class SourceImportTask(
    private val model: NLPModel
): SwingWorker<Unit, Unit>() {
    private val maxProgress = model.map { it.second }.sumOf { it.size }
    private var items = 0

    override fun doInBackground() {
        Thread { ProgressUpdateEvent("Initializing...", 0.0).dispatch() }.start()
        Main.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        API.clear()

        for((space, samples) in model) {
            for(sample in samples) {
                val data = sample()
                val sampleName = data.name

                API.addSample(data, space)
                items++

                Thread { ProgressUpdateEvent(
                    "Importing $sampleName",
                    items.toDouble() / maxProgress
                ).dispatch() }.start()
            }
        }
    }

    override fun done() {
        Toolkit.getDefaultToolkit().beep()
        Main.cursor = Cursor.getDefaultCursor()
        ProgressUpdateEvent("Done!", -1.0).dispatch()
        ModelLoadEvent().dispatch()
    }
}