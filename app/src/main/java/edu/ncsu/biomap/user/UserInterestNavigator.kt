package edu.ncsu.biomap.user

import android.content.Context

interface UserInterestNavigator {
    fun onUserInterestTypeSelected(context: Context, type: UserInterestType)
}

class DefaultUserInterestNavigator : UserInterestNavigator {
    override fun onUserInterestTypeSelected(context: Context, type: UserInterestType) {
        TODO("Not yet implemented")
    }

}