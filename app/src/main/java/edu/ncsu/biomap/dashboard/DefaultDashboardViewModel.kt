package edu.ncsu.biomap.dashboard

import android.os.Build
import android.os.Bundle
import edu.ncsu.biomap.SupportedUserType
import edu.ncsu.log.LogAdapter

class DefaultDashboardViewModel(
    private val logAdapter: LogAdapter = LogAdapter.instance(true)
) : DashboardViewModel {
    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            val type : SupportedUserType? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable("KEY", SupportedUserType::class.java)
            } else {
                it.getSerializable("KEY") as SupportedUserType?
            }
            logAdapter.i("type=[$type]")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }
}