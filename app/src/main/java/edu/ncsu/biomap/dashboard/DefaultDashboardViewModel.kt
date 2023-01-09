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
    override val bottomBarItems: MutableState<List<DashboardBottomItemType>>,
    override val currentBottomBarItem: MutableState<DashboardBottomItemType>,
    override val currentDataCollectionIntent: MutableState<TopAppBarActionType.DataCollection?>,
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
            is DashboardStore.Intent.BottomBarItemSelected -> onBottomBarItemSelected(intent)
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
            AttributeModel("Affiliation", "None", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
                "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th",
                "21th", "22th", "23th", "24th", "25th", "26th", "27th", "28th", "29th", "30th",
            )),
            AttributeModel("Weeds/Cover Crop", "None", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
            )),
            AttributeModel("Timing", "None", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
            )),
            AttributeModel("Plot ID", "None", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
            )),
            AttributeModel("Weather", "None", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
            )),
        )

        this.attributes.value = attributes
        this.cameras.value = cameras
        this.bottomBarItems.value = DashboardBottomItemType.values().toList()
    }

    private fun onAttributeSelected(intent: DashboardStore.Intent.AttributeSelected) {
        lastSelection = intent.attributeModel
        navigator.emit(intent.context, intent.attributeModel)
    }

    private fun onBottomBarItemSelected(intent: DashboardStore.Intent.BottomBarItemSelected) {
        currentBottomBarItem.value = intent.item
    }

    // endregion
}