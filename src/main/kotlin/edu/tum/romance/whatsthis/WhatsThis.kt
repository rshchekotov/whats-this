package edu.tum.romance.whatsthis

import edu.tum.romance.whatsthis.ui.ClassificationFrame

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    if(args.isEmpty()) {
       ClassificationFrame.open()
    }
}