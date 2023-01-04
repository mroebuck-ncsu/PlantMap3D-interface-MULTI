package edu.ncsu.biomap.ui.common

interface AppThemeState {
    val isDarkTheme: Boolean
    val colorPalette: ColorPalette
}

data class DefaultAppThemeState(
    override var isDarkTheme: Boolean = true,
    override var colorPalette: ColorPalette = ColorPalette.Bamboo
) : AppThemeState