package edu.tum.romance.whatsthis.kui.util

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import javax.swing.JLabel
import javax.swing.JOptionPane

object DialogUtils {
    @Suppress("unused")
    fun unimplemented() = visualInfo("Not implemented yet!", MEDIUM)

    fun visualInfo(message: String, size: Int = 0) {
        val label = JLabel(message)
        label.font = comfortaa(size)
        JOptionPane.showMessageDialog(Main, label,
            "Information", JOptionPane.INFORMATION_MESSAGE)
    }

    @Suppress("unused")
    fun visualError(message: String) {
        val label = JLabel(message)
        label.font = comfortaa(SMALL)
        JOptionPane.showMessageDialog(Main, label,
            "Error", JOptionPane.ERROR_MESSAGE)
    }

    @Suppress("unused")
    fun visualQuestion(message: String): Int {
        val label = JLabel(message)
        label.font = comfortaa(SMALL)
        return JOptionPane.showConfirmDialog(Main, label,
            "Question", JOptionPane.YES_NO_CANCEL_OPTION)
    }
}