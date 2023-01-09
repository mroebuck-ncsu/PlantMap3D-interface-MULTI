package edu.ncsu.biomap.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class DashboardBottomItemType(
    val title: String,
    val icon: ImageVector,
    val contentDescription: String,
) {
    Configuration("Configuration", Icons.Filled.Settings, "Configuration"),
    DataCollection("Data Collection", Icons.Filled.Create, "Data Collection"),
    DataReview("Data Review", Icons.Filled.List, "Data Review"),
}
