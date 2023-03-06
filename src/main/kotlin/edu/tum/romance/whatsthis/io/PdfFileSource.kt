package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object PdfFileSource: DataSource<PDDocument> {
    override fun textToVec(resource: PDDocument): IntVec = StringSource.textToVec(load(resource))

    override fun load(resource: PDDocument): String {
        val stripper = PDFTextStripper()
        return stripper.getText(resource)
    }
}