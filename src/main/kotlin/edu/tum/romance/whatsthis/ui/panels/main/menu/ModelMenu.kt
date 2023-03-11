package edu.tum.romance.whatsthis.ui.panels.main.menu

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.loader.ModelImporter
import java.awt.event.KeyEvent
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane

object ModelMenu: JMenu("Models") {
    init {
        mnemonic = KeyEvent.VK_M
        font = ClassificationFrame.fonts[0]

        val saveItem = JMenuItem("Science Distinction")
        saveItem.font = ClassificationFrame.fonts[0]
        saveItem.mnemonic = KeyEvent.VK_S
        saveItem.addActionListener {
            val label = JLabel("Are you sure you want to delete the current model?")
            label.font = ClassificationFrame.fonts[0]
            if(API.isEmpty() || JOptionPane.showConfirmDialog(
                    ClassificationFrame,
                    label,
                    "Delete Model",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                ) == JOptionPane.YES_OPTION
            ) {
                ModelImporter(
                    mapOf(
                        "Mathematics" to listOf<() -> TextData<*>>(
                            { TextData.wiki("Interpolation", "Interpolation") },
                            { TextData.wiki("Pi", "Pi") },
                            { TextData.wiki("E_(mathematical_constant)", "Euler's Number") },
                            { TextData.wiki("1_%2B_2_%2B_3_%2B_4_%2B_%E2%8B%AF", "Infinite Natural Sum") },
                            { TextData.wiki("Riemann_zeta_function", "Riemann Zeta Function") },
                            { TextData.wiki("Euler%27s_identity", "Euler's Identity") },
                            { TextData.wiki("Complex_logarithm", "Complex logarithm") },
                            { TextData.wiki("Hypercube", "Hybercube") },
                            { TextData.wiki("Tesseract", "Tesseract") },
                            { TextData.wiki("Klein_bottle", "Klein bottle") },
                            { TextData.wiki("Sierpinski_triangle", "Sierpinski Triangle") },
                            { TextData.wiki("Manifold", "Manifold") },
                            { TextData.wiki("Polyhedron", "Polyhedron") },
                            { TextData.wiki("Hyperbolic_geometry", "Hyberbolic geometry") },
                            { TextData.wiki("Mandelbrot_set", "Mandelbrot set") },
                            { TextData.wiki("Julia_set", "Julia set") },
                            { TextData.wiki("Trigonometric_functions", "Trigonometric functions") },
                        ),
                        "Biology" to listOf<() -> TextData<*>>(
                            { TextData.wiki("Heart", "Heart") },
                            { TextData.wiki("Brain", "Brain") },
                            { TextData.wiki("Lung", "Lung") },
                            { TextData.wiki("Liver", "Liver") },
                            { TextData.wiki("Kidney", "Kidney") },
                            { TextData.wiki("Stomach", "Stomach") },
                            { TextData.wiki("Pancreas", "Pancreas") },
                            { TextData.wiki("Spleen", "Spleen") },
                            { TextData.wiki("Skeletal_muscle", "Muscle") },
                            { TextData.wiki("DNA", "DNA") },
                            { TextData.wiki("RNA", "RNA") },
                            { TextData.wiki("Protein", "Protein") },
                            { TextData.wiki("Cell_(biology)", "Cell") },
                            { TextData.wiki("Tissue", "Tissue") },
                        )
                    )
                )
            }
        }
        add(saveItem)
    }
}