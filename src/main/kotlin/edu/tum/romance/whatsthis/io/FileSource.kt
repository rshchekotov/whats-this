package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import org.apache.pdfbox.Loader
import java.io.File

object FileSource: DataSource<File> {
    override fun textToVec(resource: File): IntVec = StringSource.textToVec(load(resource))

    override fun load(resource: File): String {
        if(resource.exists() && resource.isFile) {
            if(resource.extension == "pdf") {
                return PdfFileSource.load(Loader.loadPDF(resource))
            }
            return resource.readText()
        }
        error("File does not exist: ${resource.absolutePath}")
    }
}