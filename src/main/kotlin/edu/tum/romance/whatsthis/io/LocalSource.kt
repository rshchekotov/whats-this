package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import java.io.File

object LocalSource: DataSource<File> {
    override fun textToVec(resource: File): IntVec = StringSource.textToVec(load(resource))

    override fun load(resource: File): String = resource.readText()
}