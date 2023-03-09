package edu.tum.romance.whatsthis.ui.panels.main

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.components.*
import edu.tum.romance.whatsthis.ui.panels.main.menu.MenuBar
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SpringLayout

object ClassyPanel: JPanel() {
    private const val maxY = ClassificationFrame.height
    private const val maxX = ClassificationFrame.width

    private val horizontalWeights = arrayOf(
        7,  // ClassInput
        2,  // AddClassButton
        14, // SampleNameInput
        2,  // AddSampleButton
        3   // ClassifyButton
    )

    private val verticalWeights = arrayOf(
        5.75, // Editor
        0.5,  // SourceInput
        0.5,  // ClassInput
        3.25  // ClassList
    )

    private var sourceInput: JComponent

    private val horizontalDimensions = horizontalWeights.map { (it * (maxX - 10.0) / horizontalWeights.sum()).toInt() }
    private val verticalDimensions = verticalWeights.map { (it * (maxY - 10.0) / verticalWeights.sum()).toInt() }

    init {
        val layout = SpringLayout()
        this.layout = layout
        this.preferredSize = Dimension(maxX, maxY)

        add(MainViewComponent)
        MainViewComponent.preferredSize = computeSize(0..4, 0..0)
        layout.putConstraint(SpringLayout.NORTH, MainViewComponent, 5, SpringLayout.NORTH, this)
        layout.putConstraint(SpringLayout.WEST, MainViewComponent, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, MainViewComponent, -5, SpringLayout.EAST, this)

        add(SourceSelector)
        SourceSelector.preferredSize = computeSize(4..4, 1..1)
        layout.putConstraint(SpringLayout.NORTH, SourceSelector, 5, SpringLayout.SOUTH, MainViewComponent)
        layout.putConstraint(SpringLayout.EAST, SourceSelector, -5, SpringLayout.EAST, this)

        add(SourceButton)
        SourceButton.preferredSize = computeSize(3..3, 1..1)
        layout.putConstraint(SpringLayout.NORTH, SourceButton, 5, SpringLayout.SOUTH, MainViewComponent)
        layout.putConstraint(SpringLayout.EAST, SourceButton, -5, SpringLayout.WEST, SourceSelector)

        sourceInput = SourceSelector.card as JComponent
        add(sourceInput)
        sourceInput.preferredSize = computeSize(0..2, 1..1)
        layout.putConstraint(SpringLayout.NORTH, sourceInput, 5, SpringLayout.SOUTH, MainViewComponent)
        layout.putConstraint(SpringLayout.WEST, sourceInput, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, sourceInput, -5, SpringLayout.WEST, SourceButton)
        layout.putConstraint(SpringLayout.SOUTH, sourceInput, 0, SpringLayout.SOUTH, SourceSelector)

        add(ClassInput)
        ClassInput.preferredSize = computeSize(0..0, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassInput, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, ClassInput, 5, SpringLayout.WEST, this)

        add(AddClassButton)
        AddClassButton.preferredSize = computeSize(1..1, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddClassButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, AddClassButton, 5, SpringLayout.EAST, ClassInput)

        add(SampleNameInput)
        SampleNameInput.preferredSize = computeSize(2..2, 2..2)
        layout.putConstraint(SpringLayout.NORTH, SampleNameInput, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, SampleNameInput, 5, SpringLayout.EAST, AddClassButton)
        layout.putConstraint(SpringLayout.EAST, SampleNameInput, -5, SpringLayout.WEST, SourceButton)

        add(AddSampleButton)
        AddSampleButton.preferredSize = computeSize(3..3, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddSampleButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, AddSampleButton, 0, SpringLayout.WEST, SourceButton)
        layout.putConstraint(SpringLayout.EAST, AddSampleButton, 0, SpringLayout.EAST, SourceButton)

        add(ClassifyButton)
        ClassifyButton.preferredSize = computeSize(4..4, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassifyButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, ClassifyButton, 5, SpringLayout.EAST, AddSampleButton)
        layout.putConstraint(SpringLayout.EAST, ClassifyButton, -5, SpringLayout.EAST, this)

        add(ClassList)
        ClassList.preferredSize = computeSize(0..1, 3..3)
        layout.putConstraint(SpringLayout.NORTH, ClassList, 5, SpringLayout.SOUTH, ClassInput)
        layout.putConstraint(SpringLayout.WEST, ClassList, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, ClassList, 0, SpringLayout.EAST, AddClassButton)
        layout.putConstraint(SpringLayout.SOUTH, ClassList, -5, SpringLayout.SOUTH, this)

        add(SampleList)
        SampleList.preferredSize = computeSize(2..4, 3..3)
        layout.putConstraint(SpringLayout.NORTH, SampleList, 5, SpringLayout.SOUTH, ClassInput)
        layout.putConstraint(SpringLayout.WEST, SampleList, 5, SpringLayout.EAST, ClassList)
        layout.putConstraint(SpringLayout.EAST, SampleList, -5, SpringLayout.EAST, this)
        layout.putConstraint(SpringLayout.SOUTH, SampleList, -5, SpringLayout.SOUTH, this)

        ClassList.update()
        SampleList.update()
    }

    /**
     * Switch between different source input components,
     * i.e.: URL input, file input, etc.
     */
    fun updateSourceInput() {
        remove(sourceInput)
        sourceInput = SourceSelector.card as JComponent
        add(sourceInput)
        sourceInput.preferredSize = computeSize(0..2, 1..1)
        val layout = this.layout as SpringLayout
        layout.putConstraint(SpringLayout.NORTH, sourceInput, 5, SpringLayout.SOUTH, MainViewComponent)
        layout.putConstraint(SpringLayout.WEST, sourceInput, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, sourceInput, 0, SpringLayout.EAST, SampleNameInput)
        layout.putConstraint(SpringLayout.SOUTH, sourceInput, 0, SpringLayout.SOUTH, SourceSelector)
        revalidate()
        repaint()
    }

    /**
     * Operations that should happen on panel-switch.
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if(enabled) {
            ClassificationFrame.jMenuBar = MenuBar
        } else {
            ClassificationFrame.jMenuBar = null
        }
    }

    /**
     * Computing Layout Sizes based on a weighted dimension table.
     */
    private fun computeSize(x: IntRange, y: IntRange): Dimension {
        val xSize = horizontalDimensions.slice(x).sum()
        val ySize = verticalDimensions.slice(y).sum()
        return Dimension(xSize, ySize)
    }
}