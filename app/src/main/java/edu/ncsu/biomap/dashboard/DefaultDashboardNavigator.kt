package edu.ncsu.biomap.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.textinput.TextInputActivity
import edu.ncsu.biomap.ui.common.AppForResultNavigator
import edu.ncsu.log.DefaultLogAdapter
import edu.ncsu.log.LogAdapter

class DefaultDashboardNavigator(
    private val logAdapter: LogAdapter = DefaultLogAdapter(),
    override val startForResult: ActivityResultLauncher<Intent>
) : DashboardNavigator {
    override fun emit(context: Context, attributeModel: AttributeModel) {
        val bundle = Bundle()
        bundle.putSerializable(DashboardKeys.Attribute.name, attributeModel)

        navigateForResult(
            applicationContext = context,
            clazz = TextInputActivity::class.java,
            bundle = bundle,
        )
    }
}
