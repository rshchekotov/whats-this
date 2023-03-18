package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.space

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceCreateEvent
import edu.tum.romance.whatsthis.nlp.API
import javax.swing.table.AbstractTableModel

object SpaceModel: AbstractTableModel() {
    private var spaces = API.spaces().sorted().toMutableList()

    override fun getColumnCount() = 1
    override fun getRowCount() = spaces.size
    override fun getColumnName(column: Int) = "Space"
    override fun getValueAt(row: Int, column: Int): Any = spaces[row]

    @EventHandler
    fun onSpaceCreate(event: SpaceCreateEvent) {
        for(i in 0 until spaces.size) {
            if(spaces[i] > event.name) {
                spaces.add(i, event.name)
                fireTableRowsInserted(i, i)
                SpaceList.repaint()
                return
            }
        }
        spaces.add(event.name)
        fireTableDataChanged()
        SpaceList.repaint()
    }
}