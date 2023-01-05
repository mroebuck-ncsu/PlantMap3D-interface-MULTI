package edu.ncsu.biomap

import android.content.Context

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