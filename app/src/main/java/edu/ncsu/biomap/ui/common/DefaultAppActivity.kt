package edu.ncsu.biomap.ui.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import edu.ncsu.biomap.ui.theme.BioMapTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface AppActivity

abstract class DefaultAppActivity :  ComponentActivity(), AppActivity {

    private lateinit var systemUiController: SystemUiController
    private lateinit var appThemeState: MutableState<AppThemeState>
    protected val mainScope = MainScope()
    protected lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeView(this).also {
            setContentView(it)
        }.setContent {
            PostSetContent(savedInstanceState)
        }
    }

    @Composable
    private fun PostSetContent(savedInstanceState: Bundle?) {
        val isDarkMode = isSystemInDarkTheme()
        val systemUiController = rememberSystemUiController()

        this.coroutineScope = rememberCoroutineScope()
        this.systemUiController = systemUiController
        appThemeState =
            remember {
                mutableStateOf(
                    DefaultAppThemeState(
                        isDarkTheme = isDarkMode,
                        colorPalette = ColorPalette.Coral
                    )
                )
            }
        BioMapTheme(
            systemUiController = this.systemUiController,
            appThemeState = appThemeState.value,
        ) {
            MyApp(savedInstanceState)
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    @Composable
    abstract fun MyApp(savedInstanceState: Bundle?)
}