package edu.ncsu.biomap.dashboard

import android.content.Context
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.ui.common.AppForResultNavigator

interface DashboardNavigator : AppForResultNavigator {
    fun emit(context: Context, attributeModel: AttributeModel)
}
