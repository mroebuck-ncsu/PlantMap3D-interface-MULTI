package edu.ncsu.biomap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.SystemUiController
import edu.ncsu.biomap.ui.common.*
import edu.ncsu.biomap.ui.common.DarkBambooColorPalette
import edu.ncsu.biomap.ui.common.DarkCoralColorPalette
import edu.ncsu.biomap.ui.common.DarkFujiColorPalette
import edu.ncsu.biomap.ui.common.DarkJadeColorPalette
import edu.ncsu.biomap.ui.common.DarkOrenjiColorPalette
import edu.ncsu.biomap.ui.common.DarkPebbleColorPalette
import edu.ncsu.biomap.ui.common.DarkSakuraColorPalette
import edu.ncsu.biomap.ui.common.DarkSandColorPalette
import edu.ncsu.biomap.ui.common.DarkSunColorPalette
import edu.ncsu.biomap.ui.common.DarkWaveColorPalette
import edu.ncsu.biomap.ui.common.LightBambooColorPalette
import edu.ncsu.biomap.ui.common.LightCoralColorPalette
import edu.ncsu.biomap.ui.common.LightFujiColorPalette
import edu.ncsu.biomap.ui.common.LightJadeColorPalette
import edu.ncsu.biomap.ui.common.LightOrenjiColorPalette
import edu.ncsu.biomap.ui.common.LightPebbleColorPalette
import edu.ncsu.biomap.ui.common.LightSakuraColorPalette
import edu.ncsu.biomap.ui.common.LightSandColorPalette
import edu.ncsu.biomap.ui.common.LightSunColorPalette
import edu.ncsu.biomap.ui.common.LightWaveColorPalette

/**
 * @author Michael Roebuck
 * Sets the theme for this app.
 * @param systemUiController This is the specified systemUiController.
 * @param appThemeState This is the specified appThemeState preferences.
 * @param isDarkTheme This is the specified isDarkTheme indicator, which applies to the system mode.
 * @param content This is the specified content lambda.
 */
@Composable
fun BioMapTheme(
    systemUiController: SystemUiController? = null,
    appThemeState: AppThemeState? = null,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
    val canUseDarkTheme = appThemeState?.isDarkTheme == true || isDarkTheme
    val colorPalette = appThemeState?.colorPalette ?: ColorPalette.Coral
    val colors = getAppThemeColors(canUseDarkTheme, colorPalette)

    systemUiController?.apply {
        setStatusBarColor(color = colors.primary, darkIcons = isDarkTheme)
        setNavigationBarColor(color = colors.primary, darkIcons = isDarkTheme)
        setSystemBarsColor(color = colors.primary, darkIcons = isDarkTheme)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private fun getAppThemeColors(
    isDarkTheme: Boolean,
    colorPalette: ColorPalette
): Colors {
    val colors = if (isDarkTheme) {
        when (colorPalette) {
            ColorPalette.Bamboo -> DarkBambooColorPalette
            ColorPalette.Coral -> DarkCoralColorPalette
            ColorPalette.Fuji -> DarkFujiColorPalette
            ColorPalette.Jade -> DarkJadeColorPalette
            ColorPalette.Orenji -> DarkOrenjiColorPalette
            ColorPalette.Pebble -> DarkPebbleColorPalette
            ColorPalette.Sakura -> DarkSakuraColorPalette
            ColorPalette.Sand -> DarkSandColorPalette
            ColorPalette.Sun -> DarkSunColorPalette
            ColorPalette.Wave -> DarkWaveColorPalette
        }
    } else {
        when (colorPalette) {
            ColorPalette.Bamboo -> LightBambooColorPalette
            ColorPalette.Coral -> LightCoralColorPalette
            ColorPalette.Fuji -> LightFujiColorPalette
            ColorPalette.Jade -> LightJadeColorPalette
            ColorPalette.Orenji -> LightOrenjiColorPalette
            ColorPalette.Pebble -> LightPebbleColorPalette
            ColorPalette.Sakura -> LightSakuraColorPalette
            ColorPalette.Sand -> LightSandColorPalette
            ColorPalette.Sun -> LightSunColorPalette
            ColorPalette.Wave -> LightWaveColorPalette
        }
    }
    return colors
}
