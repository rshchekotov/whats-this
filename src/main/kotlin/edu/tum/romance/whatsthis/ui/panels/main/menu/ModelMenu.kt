package edu.tum.romance.whatsthis.ui.panels.main.menu

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
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
            if(Monitor.isEmpty() || JOptionPane.showConfirmDialog(
                    ClassificationFrame,
                    label,
                    "Delete Model",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                ) == JOptionPane.YES_OPTION
            ) {
                ModelImporter(
                    mapOf(
                        "Mathematics" to listOf<Pair<String, () -> TextData<*>>>(
                            "Interpolation" to { TextData.wiki("Interpolation") },
                            "Pi" to { TextData.wiki("Pi") },
                            "Euler's Number" to { TextData.wiki("E_(mathematical_constant)") },
                            "Infinite Natural Sum" to { TextData.wiki("1_%2B_2_%2B_3_%2B_4_%2B_%E2%8B%AF") },
                            "Riemann Zeta Function" to { TextData.wiki("Riemann_zeta_function") },
                            "Euler's Identity" to { TextData.wiki("Euler%27s_identity") },
                            "Complex Logarithm" to { TextData.wiki("Complex_logarithm") },
                            "Hybercube" to { TextData.wiki("Hypercube") },
                            "Tesseract" to { TextData.wiki("Tesseract") },
                            "Klein Bottle" to { TextData.wiki("Klein_bottle") },
                            "Sierpinski Triangle" to { TextData.wiki("Sierpinski_triangle") },
                            "Manifold" to { TextData.wiki("Manifold") },
                            "Polyhedron" to { TextData.wiki("Polyhedron") },
                            "Hyperbolic Geometry" to { TextData.wiki("Hyperbolic_geometry") },
                            "Mandelbrot Set" to { TextData.wiki("Mandelbrot_set") },
                            "Julia Set" to { TextData.wiki("Julia_set") },
                            "Trigonometric Functions" to { TextData.wiki("Trigonometric_functions") },
                        ),
                        "Biology" to listOf<Pair<String, () -> TextData<*>>>(
                            "Heart" to { TextData.wiki("Heart") },
                            "Brain" to { TextData.wiki("Brain") },
                            "Lung" to { TextData.wiki("Lung") },
                            "Liver" to { TextData.wiki("Liver") },
                            "Kidney" to { TextData.wiki("Kidney") },
                            "Stomach" to { TextData.wiki("Stomach") },
                            "Pancreas" to { TextData.wiki("Pancreas") },
                            "Spleen" to { TextData.wiki("Spleen") },
                            "Muscle" to { TextData.wiki("Skeletal_muscle") },
                            "DNA" to { TextData.wiki("DNA") },
                            "RNA" to { TextData.wiki("RNA") },
                            "Protein" to { TextData.wiki("Protein") },
                            "Cell" to { TextData.wiki("Cell_(biology)") },
                            "Tissue" to { TextData.wiki("Tissue") },
                        )
                    )
                )
            }
        }
        add(saveItem)
    }
}