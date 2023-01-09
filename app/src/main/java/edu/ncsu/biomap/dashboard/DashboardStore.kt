package edu.ncsu.biomap.dashboard

import android.content.Context
import edu.ncsu.biomap.model.AttributeModel

sealed class DashboardStore {
    sealed class Intent {
        data class AttributeSelected(
            val context: Context,
            val attributeModel: AttributeModel,
        ) : Intent()
    }
    sealed class State {
        class AttributeUpdated(val list: List<AttributeModel>): State()
        object Idle: State()
    }
}
