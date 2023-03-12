package edu.tum.romance.whatsthis.ui.views.main.components.space

import javax.swing.JScrollPane

object SpaceListPane: JScrollPane(
    SpaceList,
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
)