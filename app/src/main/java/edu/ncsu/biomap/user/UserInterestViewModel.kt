package edu.ncsu.biomap.user

import android.content.Context

interface UserInterestViewModel {
    fun onUserInterestTypeSelected(context: Context, type: UserInterestType)
}

class DefaultUserInterestViewModel(
    private val navigator: DefaultUserInterestNavigator
) : UserInterestViewModel {
    override fun onUserInterestTypeSelected(context: Context, type: UserInterestType) {
        navigator.onUserInterestTypeSelected(context, type)
    }
}