package edu.tum.romance.whatsthis.kui

import com.formdev.flatlaf.FlatLightLaf
import edu.tum.romance.whatsthis.kui.scenes.View
import org.apache.logging.log4j.LogManager
import javax.swing.JFrame
import javax.swing.UIManager

object Main: JFrame() {
    private var width: Int
    private var height: Int
    private val logger = LogManager.getLogger(Main::class.java)

    var currentView: View? = null

    init {
        val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
        width = (screenSize.width * 0.8).toInt()
        height = (screenSize.height * 0.8).toInt()

        title = "What's This?"
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        try {
            UIManager.setLookAndFeel(FlatLightLaf())
        } catch (e: Exception) {
            logger.error("Failed to load Modern Look and Feel.", e)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        this.isVisible = true
    }
}