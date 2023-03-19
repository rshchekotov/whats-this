package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.FixedSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceSelectEvent
import edu.tum.romance.whatsthis.nlp.API
import javax.swing.table.AbstractTableModel

object VectorModel: AbstractTableModel() {
    private var space: String? = null
    internal var samples = API.classified().sorted().toMutableList()

    override fun getColumnCount() = 1
    override fun getRowCount() = samples.size
    override fun getColumnName(column: Int) = if (space == null) "Samples" else "$space Samples"
    override fun getValueAt(row: Int, column: Int): Any = samples[row]

    @EventHandler
    fun onSpaceSelect(event: SpaceSelectEvent) {
        space = event.name
        samples = API.spaceVectors(event.name).sorted().toMutableList()
        fireTableDataChanged()
        VectorList.repaint()
    }

    @EventHandler(SpaceDeselectEvent::class)
    fun onSpaceDeselect() {
        space = null
        samples = API.classified().sorted().toMutableList()
        fireTableDataChanged()
        VectorList.repaint()
    }

    @EventHandler
    fun onFixedSampleCreate(event: FixedSampleCreateEvent) {
        if(space == null || space == event.space) {
            addSampleIfNotExists(event.data.name)
        }
    }

    @EventHandler(ModelLoadEvent::class)
    fun onModelLoad() {
        samples = API.classified().sorted().toMutableList()
        fireTableDataChanged()
        VectorList.repaint()
    }

    private fun addSampleIfNotExists(sample: String) {
        for(i in 0 until samples.size) {
            if(samples[i] == sample) return
            if(samples[i] > sample) {
                samples.add(i, sample)
                fireTableRowsInserted(i, i)
                VectorList.repaint()
                return
            }
        }
        samples.add(sample)
        fireTableRowsInserted(samples.size - 1, samples.size - 1)
        VectorList.repaint()
    }
}