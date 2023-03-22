package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.dictionary

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.FixedSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.data.VariableSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.v1.nlp.API
import javax.swing.AbstractListModel

object DictionaryModel: AbstractListModel<String>() {
    private var words = API.words()
    override fun getSize(): Int = words.size
    override fun getElementAt(index: Int): String = words[index]

    @EventHandler(
        FixedSampleCreateEvent::class,
        VariableSampleCreateEvent::class,
        ModelLoadEvent::class
    )
    fun onWordChange() {
        words = API.words()
        fireContentsChanged(this, 0, words.size)
    }
}