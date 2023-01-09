package edu.ncsu.biomap

import android.content.Context
import androidx.lifecycle.ViewModel

class DefaultMainViewModel(
    private val navigator: MainNavigator = DefaultMainNavigator()
) : ViewModel(), MainViewModel {
    override val usersResIds = listOf(
        SupportedUserType.Calibrator(),
        SupportedUserType.Researcher(),
        SupportedUserType.Farmer(),
    )

    override fun emit(context: Context, type: SupportedUserType) {
        println("${this::class.simpleName}: New Intent Received=[$type]")
        navigator.emit(context, type)
    }
}