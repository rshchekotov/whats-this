package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.kui.components.SymbolicButton
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.VariablePane
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualInfo
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.nlp.API
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

internal object VariableSampleRenderer: DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        if(column == 1 && value is String) {
            if(value in VariablePane.buttonCache) {
                val (button, _) = VariablePane.buttonCache[value]!!
                return button
            }

            val button = SymbolicButton(MEDIUM, "?", "Information on $value") {
                var distances = API.distances(value)
                val sum = distances.sum()
                distances = distances.map { 1 - it / sum }

                val classifications = distances.mapIndexed { index, distance ->
                    val space = "<td>${API.spaces()[index]}</td>"
                    val dist = "<td>${String.format("%.2f%%", distance * 100)}</td>"
                    "<tr>$space$dist</tr>"
                }.joinToString("")

                visualInfo(display(value, classifications))
            }
            button.isOpaque = true
            VariablePane.buttonCache[value] = button to false
            return button
        }
        return component
    }

    private fun display(value: Any?, classifications: String) = """
                        <html>
                            <h1>Document: $value</h1>
                            <table>
                                <tr>
                                    <th>Space</th>
                                    <th>Relation</th>
                                </tr>
                                $classifications
                            </table>
                        </html>
                    """.trimIndent()
}