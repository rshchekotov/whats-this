package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.space

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.FixedSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceCreateEvent
import edu.tum.romance.whatsthis.v1.nlp.API
import javax.swing.table.AbstractTableModel

object SpaceModel: AbstractTableModel() {
    internal var spaces = API.spaces().sorted().toMutableList()

    override fun getColumnCount() = 1
    override fun getRowCount() = spaces.size
    override fun getColumnName(column: Int) = "Space"
    override fun getValueAt(row: Int, column: Int): Any = spaces[row]

    @EventHandler
    fun onSpaceCreate(event: SpaceCreateEvent) {
        addSpaceIfNotExists(event.name)
    }

    @EventHandler
    fun onFixedSampleCreate(event: FixedSampleCreateEvent) {
        addSpaceIfNotExists(event.space)
    }

    @EventHandler(ModelLoadEvent::class)
    fun onModelLoad() {
        spaces = API.spaces().sorted().toMutableList()
        fireTableDataChanged()
        SpaceList.repaint()
    }

    private fun addSpaceIfNotExists(space: String) {
        for(i in 0 until spaces.size) {
            if(spaces[i] == space) return
            if(spaces[i] > space) {
                spaces.add(i, space)
                fireTableRowsInserted(i, i)
                SpaceList.repaint()
                return
            }
        }
        spaces.add(space)
        fireTableRowsInserted(spaces.size - 1, spaces.size - 1)
        SpaceList.repaint()
    }
}