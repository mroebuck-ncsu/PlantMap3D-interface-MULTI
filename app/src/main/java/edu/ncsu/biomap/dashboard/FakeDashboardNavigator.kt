package edu.ncsu.biomap.dashboard

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import edu.ncsu.biomap.model.AttributeModel

class FakeDashboardNavigator : DashboardNavigator {

    var totalEmitInvocations = 0

    override fun emit(context: Context, attributeModel: AttributeModel) {
        totalEmitInvocations++
    }

    override val startForResult: ActivityResultLauncher<Intent>
        get() = TODO("Not yet implemented")
}