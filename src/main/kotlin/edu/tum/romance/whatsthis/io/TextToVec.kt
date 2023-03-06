package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec

interface TextToVec<T> {
    fun textToVec(resource: T): IntVec
}