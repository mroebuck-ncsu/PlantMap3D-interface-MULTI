package edu.ncsu.biomap.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import edu.ncsu.biomap.textinput.TextInputActivity

interface AppForResultNavigator : AppNavigator {
    val startForResult: ActivityResultLauncher<Intent>

    fun navigateForResult(
        applicationContext: Context,
        clazz: Class<TextInputActivity>,
        bundle: Bundle?,
    ) {
        val intent = Intent(applicationContext, clazz)
        bundle?.let { intent.putExtras(it) }
        startForResult.launch(intent)
    }
}