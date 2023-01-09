package edu.ncsu.biomap.dashboard

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.log.LogAdapter
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultDashboardViewModel(
    private val logAdapter: LogAdapter = LogAdapter.instance(true),
    private val navigator: DashboardNavigator,
    override val cameras: MutableState<List<String>>,
    override val attributes: MutableState<List<AttributeModel>>,
) : ViewModel(), DashboardViewModel {

    // region Override Methods and Properties

    private val _stateFlow: MutableStateFlow<DashboardStore.State> =
        MutableStateFlow(DashboardStore.State.Idle)
    override val stateFlow: MutableStateFlow<DashboardStore.State> = _stateFlow

    private var lastSelection: AttributeModel? = null

    init {
        start()
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { }
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun emit(intent: DashboardStore.Intent) {
        when(intent) {
            is DashboardStore.Intent.AttributeSelected -> onAttributeSelected(intent)
        }
    }

    override fun updateAttribute(attributeModel: AttributeModel?) {
        val updatedList = attributes.value.toMutableList()
        updatedList.replaceAll {
            return@replaceAll if(it.attribute == lastSelection?.attribute) {
                it.copy(value = attributeModel?.value ?: "")
            } else {
                it
            }
        }
        attributes.value = updatedList.toList()
    }

    // endregion

    // region Private Methods

    private fun start() {
        val cameras: List<String> = listOf(
            "Oak-D Pro",
            "Oak-D Pro W",
            "Oad-D S2",
            "Oak-D SW",
            "Oak-D Lite",
            "Oak-D",
            "Oak-1",
            "Oak-1 W",
            "Oak-1 Lite",
            "Oak-1 Lite W",
            "Oak-D Pro PoE",
            "Oak-D Pro W PoE",
            "Oad-D S2 PoE",
            "Oak-D W PoE",
            "Oak-D PoE",
            "Oak-1 PoE",
        )

        val attributes: List<AttributeModel> = listOf(
            AttributeModel("Affiliation", "None"),
            AttributeModel("Weeds/Cover Crop", "None"),
            AttributeModel("Timing", "None"),
            AttributeModel("Plot ID", "None"),
            AttributeModel("Weather", "None"),
        )

        this.attributes.value = attributes
        this.cameras.value = cameras
    }

    private fun onAttributeSelected(intent: DashboardStore.Intent.AttributeSelected) {
        lastSelection = intent.attributeModel
        navigator.emit(intent.context, intent.attributeModel)
    }

    // endregion
}