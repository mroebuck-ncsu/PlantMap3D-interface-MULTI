package edu.ncsu.biomap.dashboard

import android.os.Bundle

interface DashboardViewModel {
    fun restoreInstanceState(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
}
