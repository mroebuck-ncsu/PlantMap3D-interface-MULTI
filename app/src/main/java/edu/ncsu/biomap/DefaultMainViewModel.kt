package edu.ncsu.biomap

import android.content.Context
import org.kodein.di.samples.edu.ncsu.biomap.MainViewModel

class DefaultMainViewModel(
    private val navigator: MainNavigator = DefaultMainNavigator()
) : MainViewModel {
    override val usersResIds = listOf(
        SupportedUserType.Calibrator(),
        SupportedUserType.Researcher(),
        SupportedUserType.Farmer(),
    )

    override fun emit(context: Context, type: SupportedUserType) {
        navigator.emit(context, type)
    }
}