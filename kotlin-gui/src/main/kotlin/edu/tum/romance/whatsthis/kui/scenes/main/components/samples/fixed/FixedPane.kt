package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed

import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.space.SpaceList
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector.VectorList
import edu.tum.romance.whatsthis.kui.util.times
import java.awt.Dimension
import javax.swing.JSplitPane

object FixedPane: JSplitPane(HORIZONTAL_SPLIT, SpaceList, VectorList) {
    override fun setPreferredSize(preferredSize: Dimension) {
        super.setPreferredSize(preferredSize)
        SpaceList.preferredSize = preferredSize * (0.4 to 1.0)
        VectorList.preferredSize = preferredSize * (0.6 to 1.0)
        this.resizeWeight = 0.4
    }
}