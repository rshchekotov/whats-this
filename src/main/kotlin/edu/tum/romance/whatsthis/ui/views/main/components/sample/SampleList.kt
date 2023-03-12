package edu.tum.romance.whatsthis.ui.views.main.components.sample

import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.ClassifiedListModel
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.SampleListPane
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.UnclassifiedListModel
import javax.swing.JScrollPane
import javax.swing.JTabbedPane

object SampleList: JTabbedPane() {
    private val classifiedList = SampleListPane(ClassifiedListModel)
    private val classifiedPane = JScrollPane(classifiedList)
    private val unclassifiedList = SampleListPane(UnclassifiedListModel)
    private val unclassifiedPane = JScrollPane(unclassifiedList)

    init {
        addTab("Classified Samples", classifiedPane)
        addTab("Unclassified Samples", unclassifiedPane)
        addChangeListener {
            if (selectedIndex == 0) {
                unclassifiedList.onUnload()
                classifiedList.onLoad()
            } else if (selectedIndex == 1) {
                classifiedList.onUnload()
                unclassifiedList.onLoad()
            }
        }
    }
}