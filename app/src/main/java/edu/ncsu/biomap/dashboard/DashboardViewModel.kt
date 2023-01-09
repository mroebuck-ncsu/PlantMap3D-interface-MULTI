package edu.ncsu.biomap.dashboard

import android.os.Bundle
import androidx.compose.runtime.MutableState
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.textinput.TextInputStore
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.Serializable

interface DashboardViewModel {
    val stateFlow: MutableStateFlow<DashboardStore.State>
    val attributes: MutableState<List<AttributeModel>>
    val cameras: MutableState<List<String>>
    val bottomBarItems: MutableState<List<DashboardBottomItemType>>
    val currentBottomBarItem: MutableState<DashboardBottomItemType>
    val currentDataCollectionIntent: MutableState<TopAppBarActionType.DataCollection?>

    fun restoreInstanceState(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
    fun emit(intent: DashboardStore.Intent)
    fun updateAttribute(attributeModel: AttributeModel?)
}
