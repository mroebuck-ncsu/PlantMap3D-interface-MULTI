package edu.ncsu.biomap

import android.content.Context
import edu.ncsu.biomap.ui.common.AppNavigator

interface MainNavigator : AppNavigator {
    fun emit(context: Context, type: SupportedUserType)
}
