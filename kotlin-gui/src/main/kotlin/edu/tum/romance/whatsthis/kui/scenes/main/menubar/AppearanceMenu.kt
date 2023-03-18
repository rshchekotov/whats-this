package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.FlatLaf
import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent.*
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.SwingUtilities
import javax.swing.UIManager

object AppearanceMenu: JMenu("Appearance") {
    private val logger = LogManager.getLogger(AppearanceMenu::class.java)
    private val lookAndFeels: Array<Pair<Pair<String, Int>, () -> FlatLaf>> = arrayOf(
        ("Light" to VK_L) to ::FlatLightLaf,
        ("Dark" to VK_D) to ::FlatDarkLaf,
        ("Darcula" to VK_C) to ::FlatDarculaLaf
    )

    init {
        font = comfortaa(SMALL)

        for(laf in lookAndFeels) {
            val (name, mnemonic) = laf.first
            val lafClass = laf.second
            val menuItem = JMenuItem(name)
            menuItem.font = comfortaa(SMALL)
            menuItem.mnemonic = mnemonic
            menuItem.addActionListener { setLookAndFeel(lafClass, name) }
            add(menuItem)
        }
    }

    fun setLookAndFeel(lafClass: () -> FlatLaf, name: String) {
        try {
            UIManager.setLookAndFeel(lafClass())
            SwingUtilities.updateComponentTreeUI(Main)
            SwingUtilities.updateComponentTreeUI(TopBar)
        } catch (e: Exception) {
            logger.error("Failed to load '$name' Modern Look and Feel.", e)
        }
    }
}