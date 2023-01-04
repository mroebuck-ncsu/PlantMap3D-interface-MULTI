package edu.ncsu.biomap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import edu.ncsu.biomap.dashboard.DashboardActivity

class DefaultMainNavigator : MainNavigator {
    override fun emit(context: Context, type: SupportedUserType) {
        val bundle = Bundle()
        bundle.putSerializable("KEY", type)

        navigate(
            applicationContext = context,
            clazz = DashboardActivity::class.java,
            bundle = bundle,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }
}