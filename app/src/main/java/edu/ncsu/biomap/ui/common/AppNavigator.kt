package edu.ncsu.biomap.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle

interface AppNavigator {
    fun <T> navigate(
        applicationContext: Context,
        clazz: Class<T>,
        flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
        bundle: Bundle? = null
    ) {
        val intent = Intent(applicationContext, clazz)
        intent.flags = flags
        bundle?.let { intent.putExtras(it) }
        applicationContext.startActivity(intent)
    }
}