package edu.tum.romance.whatsthis.kui.event.events.progress

import edu.tum.romance.whatsthis.kui.event.Event

/**
 * Event that is dispatched when the progress of the source import task changes.
 * @param comment Progress Text to display
 * @param percentage Progress percentage to display
 */
class ProgressUpdateEvent(val comment: String, val percentage: Double): Event()