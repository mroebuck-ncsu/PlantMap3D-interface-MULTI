package edu.ncsu.biomap

import android.content.Context
import org.kodein.di.samples.edu.ncsu.biomap.ui.common.AppNavigator

interface MainNavigator : AppNavigator {
    fun emit(context: Context, type: SupportedUserType)

}
