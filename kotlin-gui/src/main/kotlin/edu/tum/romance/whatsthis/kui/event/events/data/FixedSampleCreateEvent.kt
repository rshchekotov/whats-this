package edu.tum.romance.whatsthis.kui.event.events.data

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.kui.event.Event

class FixedSampleCreateEvent(val data: TextData<*>, val space: String): Event()