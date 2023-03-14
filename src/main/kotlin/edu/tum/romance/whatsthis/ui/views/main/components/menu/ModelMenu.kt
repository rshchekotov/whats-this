package edu.tum.romance.whatsthis.ui.views.main.components.menu

import edu.tum.romance.whatsthis.io.data.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualQuestion
import edu.tum.romance.whatsthis.ui.views.main.components.menu.importer.SourceImportTask
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem

object ModelMenu: JMenu("Model") {
    init {
        font = ClassificationFrame.fonts[0]

        val science = JMenuItem("Science Distinction")
        science.font = ClassificationFrame.fonts[0]
        science.mnemonic = KeyEvent.VK_S
        science.addActionListener {
            if(API.isEmpty() || visualQuestion("Are you sure you want to delete the current model?")) {
                val task = SourceImportTask(mutableListOf(
                    "Mathematics" to listOf<() -> TextData<*>>(
                        { TextData.wiki("Interpolation", "Interpolation") },
                        { TextData.wiki("Pi", "Pi") },
                        { TextData.wiki("E_(mathematical_constant)", "Euler's Number") },
                        { TextData.wiki("1_%2B_2_%2B_3_%2B_4_%2B_%E2%8B%AF", "Infinite Natural Sum") },
                        { TextData.wiki("Riemann_zeta_function", "Riemann Zeta Function") },
                        { TextData.wiki("Euler%27s_identity", "Euler's Identity") },
                        { TextData.wiki("Complex_logarithm", "Complex Logarithm") },
                        { TextData.wiki("Hypercube", "Hybercube") },
                        { TextData.wiki("Tesseract", "Tesseract") },
                        { TextData.wiki("Klein_bottle", "Klein Bottle") },
                        { TextData.wiki("Sierpinski_triangle", "Sierpinski Triangle") },
                        { TextData.wiki("Manifold", "Manifold") },
                        { TextData.wiki("Polyhedron", "Polyhedron") },
                        { TextData.wiki("Hyperbolic_geometry", "Hyperbolic Geometry") },
                        { TextData.wiki("Mandelbrot_set", "Mandelbrot Set") },
                        { TextData.wiki("Julia_set", "Julia Set") },
                        { TextData.wiki("Trigonometric_functions", "Trigonometric Functions") },
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
                task.execute()
            }
        }
        add(science)
    }
}