package edu.tum.romance.whatsthis.ui.views.main

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame.componentSize
import edu.tum.romance.whatsthis.ui.ClassificationFrame.unimplemented
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualQuestion
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.components.SymbolicButton
import edu.tum.romance.whatsthis.ui.views.View
import edu.tum.romance.whatsthis.ui.views.main.components.main.DataTextView
import edu.tum.romance.whatsthis.ui.views.main.components.main.DataVectorView
import edu.tum.romance.whatsthis.ui.views.main.components.sample.SampleInput
import edu.tum.romance.whatsthis.ui.views.main.components.sample.SampleList
import edu.tum.romance.whatsthis.ui.views.main.components.source.SourceSelector
import edu.tum.romance.whatsthis.ui.views.main.components.source.io.FileSourceInput
import edu.tum.romance.whatsthis.ui.views.main.components.source.io.WebSourceInput
import edu.tum.romance.whatsthis.ui.views.main.components.space.SpaceInput
import edu.tum.romance.whatsthis.ui.views.main.components.space.SpaceListPane
import edu.tum.romance.whatsthis.util.observer.Observable
import edu.tum.romance.whatsthis.util.observer.trigger
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.SpringLayout

object MainView: View() {
    //#region Dimensions
    private val weights = arrayOf(
        arrayOf(7, 2, 14, 2, 3),
        arrayOf(23, 2, 2, 13)
    )
    private val xFactor = weights[0].sum()
    private val yFactor = weights[1].sum()
    //#endregion

    //#region UI Model
    val content = Observable<TextData<*>?>(null)
    val contentName = Observable("")

    val selectedData = Observable(-1)
    val dataUpdateAttempt = Observable(false)
    val dataUpdate = Observable(false)
    val selectedSpace = Observable(-1)
    val spaceUpdate = Observable(false)
    //#endregion

    //#region Data Views
    private val dataView: JComponent
        get() = dataViews[selectedDataViewType.value].second as JComponent
    val dataViews = arrayOf("Text" to DataTextView, "Word Vector" to DataVectorView)
    val dataViewSize = componentSize(0 to 4, 0 to 0)
    val selectedDataViewType = Observable(0)
    //#endregion

    //#region Source Input
    private val sourceInput: JComponent
        get() = sources[selectedSourceType.value].second as JComponent
    val sources = arrayOf("Web" to WebSourceInput, "File" to FileSourceInput)
    val selectedSourceType = Observable(0)
    //#endregion

    //#region Components
    private val sourceButton = SymbolicButton("⏎", "Load data from source") {
        (sourceInput as Runnable).run()
    }

    private val spaceButton = SymbolicButton("⏎", "Create Vector Space") {
        (SpaceInput as Runnable).run()
    }

    private val sampleButton = SymbolicButton("⏎", "Create Sample") {
        (SampleInput as Runnable).run()
    }

    private val classifyButton = SymbolicButton("✓", "Classify the sample") {
        unimplemented()
    }
    //#endregion

    init {
        val layout = SpringLayout()
        this.layout = layout
        this.preferredSize = componentSize(0 to 4, 0 to 3)

        setupMainView(-1 to 0)

        setupSourceInput(-1 to 0)

        add(SourceSelector)
        SourceSelector.preferredSize = componentSize(4 to 4, 1 to 1)
        layout.putConstraint(SpringLayout.NORTH, SourceSelector, 5, SpringLayout.SOUTH, dataView)
        layout.putConstraint(SpringLayout.EAST, SourceSelector, -5, SpringLayout.EAST, this)

        add(sourceButton)
        sourceButton.preferredSize = componentSize(3 to 3, 1 to 1)
        layout.putConstraint(SpringLayout.NORTH, sourceButton, 0, SpringLayout.NORTH, SourceSelector)
        layout.putConstraint(SpringLayout.EAST, sourceButton, -5, SpringLayout.WEST, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, sourceButton, 5, SpringLayout.EAST, sourceInput)

        add(SpaceInput)
        SpaceInput.preferredSize = componentSize(0 to 0, 2 to 2)
        layout.putConstraint(SpringLayout.NORTH, SpaceInput, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, SpaceInput, 5, SpringLayout.WEST, this)

        add(spaceButton)
        spaceButton.preferredSize = componentSize(1 to 1, 2 to 2)
        layout.putConstraint(SpringLayout.NORTH, spaceButton, 0, SpringLayout.NORTH, SpaceInput)
        layout.putConstraint(SpringLayout.WEST, spaceButton, 5, SpringLayout.EAST, SpaceInput)


        add(SampleInput)
        SampleInput.preferredSize = componentSize(2 to 2, 2 to 2)
        layout.putConstraint(SpringLayout.NORTH, SampleInput, 0, SpringLayout.NORTH, spaceButton)
        layout.putConstraint(SpringLayout.WEST, SampleInput, 5, SpringLayout.EAST, spaceButton)
        layout.putConstraint(SpringLayout.EAST, SampleInput, 0, SpringLayout.EAST, sourceInput)

        add(sampleButton)
        sampleButton.preferredSize = componentSize(3 to 3, 2 to 2)
        layout.putConstraint(SpringLayout.NORTH, sampleButton, 0, SpringLayout.NORTH, SampleInput)
        layout.putConstraint(SpringLayout.WEST, sampleButton, 5, SpringLayout.EAST, SampleInput)
        layout.putConstraint(SpringLayout.EAST, sampleButton, 0, SpringLayout.EAST, sourceButton)

        add(classifyButton)
        classifyButton.preferredSize = componentSize(4 to 4, 2 to 2)
        layout.putConstraint(SpringLayout.NORTH, classifyButton, 0, SpringLayout.NORTH, sampleButton)
        layout.putConstraint(SpringLayout.WEST, classifyButton, 0, SpringLayout.WEST, SourceSelector)
        layout.putConstraint(SpringLayout.EAST, classifyButton, -5, SpringLayout.EAST, this)

        add(SpaceListPane)
        SpaceListPane.preferredSize = componentSize(0 to 1, 3 to 3)
        layout.putConstraint(SpringLayout.NORTH, SpaceListPane, 5, SpringLayout.SOUTH, SpaceInput)
        layout.putConstraint(SpringLayout.WEST, SpaceListPane, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.SOUTH, SpaceListPane, -5, SpringLayout.SOUTH, this)
        layout.putConstraint(SpringLayout.EAST, SpaceListPane, 0, SpringLayout.EAST, spaceButton)

        add(SampleList)
        SampleList.preferredSize = componentSize(2 to 4, 3 to 3)
        layout.putConstraint(SpringLayout.NORTH, SampleList, 0, SpringLayout.NORTH, SpaceListPane)
        layout.putConstraint(SpringLayout.WEST, SampleList, 5, SpringLayout.EAST, SpaceListPane)
        layout.putConstraint(SpringLayout.SOUTH, SampleList, 0, SpringLayout.SOUTH, SpaceListPane)
        layout.putConstraint(SpringLayout.EAST, SampleList, -5, SpringLayout.EAST, this)
    }

    private fun setupMainView(change: Pair<Int, Int>) {
        val (old, new) = change
        if (old != -1) {
            (dataViews[old].second as Loadable).onUnload()
            remove(dataViews[old].second as JComponent)
        }
        if (new != -1) {
            val component = dataViews[new].second as JComponent
            val layout = this.layout as SpringLayout

            (dataViews[new].second as Loadable).onLoad()
            add(component)

            component.preferredSize = dataViewSize
            layout.putConstraint(SpringLayout.WEST, component, 5, SpringLayout.WEST, this)
            layout.putConstraint(SpringLayout.NORTH, component, 5, SpringLayout.NORTH, this)
            layout.putConstraint(SpringLayout.EAST, component, -5, SpringLayout.EAST, this)

            if(old != -1) {
                layout.putConstraint(SpringLayout.NORTH, sourceInput, 5, SpringLayout.SOUTH, component)
            }
        }
    }

    private fun setupSourceInput(change: Pair<Int, Int>) {
        val (old, new) = change
        if (old != -1) {
            remove(sources[old].second as JComponent)
        }
        if (new != -1) {
            val layout = this.layout as SpringLayout
            add(sourceInput)

            sourceInput.preferredSize = componentSize(0 to 2, 1 to 1)
            layout.putConstraint(SpringLayout.WEST, sourceInput, 5, SpringLayout.WEST, this)
            layout.putConstraint(SpringLayout.NORTH, sourceInput, 5, SpringLayout.SOUTH, dataView)

            if(old != -1) {
                layout.putConstraint(SpringLayout.EAST, SampleInput, 0, SpringLayout.EAST, sourceInput)

                layout.putConstraint(SpringLayout.WEST, sourceButton, 5, SpringLayout.EAST, sourceInput)
            }
        }
    }

    private fun componentSize(xStretch: Pair<Int, Int>, yStretch: Pair<Int, Int>): Dimension {
        val x = weights[0].slice(xStretch.first .. xStretch.second).sum()
        val y = weights[1].slice(yStretch.first .. yStretch.second).sum()
        return componentSize(x.toDouble() / xFactor to y.toDouble() / yFactor)
    }

    private fun createSample() {
        if(selectedSpace.value == -1) {
            val create =
                visualQuestion("No Vector Space has been selected, would you like to create a variable sample?")
            if (!create) return
        }

        val data = content.value
        if(data == null) {
            visualError("No data has been loaded")
            return
        }
        val name = contentName.value
        if(name == "") {
            visualError("No name has been given to the data")
            return
        }
        data.name = name

        if(selectedSpace.value == -1) {
            API.addSample(data)
        } else {
            API.addSample(data, API.spaces()[selectedSpace.value])
        }
        contentName.value = ""
        content.value = null

        dataUpdate.trigger()
    }

    override fun onLoad() {
        selectedSourceType.observe(1, ::setupSourceInput)
        selectedDataViewType.observe(1, ::setupMainView)

        dataUpdateAttempt.observe(1) { createSample() }
    }

    override fun onUnload() {
        selectedSourceType.stopObserving(1)
        selectedDataViewType.stopObserving(1)
        dataUpdateAttempt.stopObserving(1)
    }
}