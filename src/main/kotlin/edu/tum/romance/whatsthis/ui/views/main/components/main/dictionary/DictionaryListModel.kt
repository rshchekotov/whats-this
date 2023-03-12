package edu.tum.romance.whatsthis.ui.views.main.components.main.dictionary

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.AbstractListModel

object DictionaryListModel: AbstractListModel<String>(), Loadable {
    override fun getSize() = API.vocabulary.size()
    override fun getElementAt(index: Int) = API.vocabulary.words()[index]
    override fun onLoad() {
        MainView.dataUpdate.observe(3) {
            fireContentsChanged(this, 0, size)
        }

        MainView.content.observe(3) {
            fireContentsChanged(this, 0, size)
        }
    }

    override fun onUnload() {
        MainView.dataUpdate.stopObserving(3)
        MainView.content.stopObserving(3)
    }
}