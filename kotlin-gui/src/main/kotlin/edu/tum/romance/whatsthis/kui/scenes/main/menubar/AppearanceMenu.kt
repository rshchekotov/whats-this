package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.FlatLightLaf
import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.scenes.main.MainView
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.VariablePane
import edu.tum.romance.whatsthis.kui.scenes.menu.MenuView
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent.*
import javax.swing.JComponent
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
            /* Frame */
            SwingUtilities.updateComponentTreeUI(Main)
            /* Views */
            SwingUtilities.updateComponentTreeUI(MenuView)
            SwingUtilities.updateComponentTreeUI(MainView)
            /* Bar */
            SwingUtilities.updateComponentTreeUI(TopBar)
            /* Data Views */
            MainPane.renders.map { it.second as JComponent }
                .forEach(SwingUtilities::updateComponentTreeUI)
            /* Button Cache */
            VariablePane.buttonCache.values.map { it.first }
                .forEach(SwingUtilities::updateComponentTreeUI)
        } catch (e: Exception) {
            logger.error("Failed to load '$name' Modern Look and Feel.", e)
        }
    }
}