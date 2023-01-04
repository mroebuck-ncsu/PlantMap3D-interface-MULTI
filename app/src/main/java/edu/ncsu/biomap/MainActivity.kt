package edu.ncsu.biomap

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ncsu.biomap.ui.theme.BioMapTheme
import org.kodein.di.samples.edu.ncsu.biomap.MainViewModel
import edu.ncsu.biomap.ui.common.ColorPalette
import edu.ncsu.biomap.ui.common.DefaultAppActivity
import edu.ncsu.biomap.ui.common.DefaultAppThemeState

class MainActivity : DefaultAppActivity() {

    private lateinit var viewModel: MutableState<MainViewModel>

    @Composable
    override fun MyApp(savedInstanceState: Bundle?) {
        InitViewModel()
        TopView(
            viewModel = viewModel.value,
            context = this,
        )
    }

    @Composable
    private fun InitViewModel() {
        viewModel = remember {
            mutableStateOf(DefaultMainViewModel())
        }
    }
}

@Composable
private fun TopView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    context: Context?,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 8.dp)
        ) {
            Box(modifier = modifier) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(R.string.user_type_instructions),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                )
                Divider(
                    modifier = modifier,
                    color = Color.LightGray
                )
            }

            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = viewModel.usersResIds,
                    itemContent = {
                        CreateUserTypeOption(modifier, context, viewModel, it)
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateUserTypeOption(
    modifier: Modifier,
    context: Context?,
    viewModel: MainViewModel,
    type: SupportedUserType
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Button(onClick = { context?.let { viewModel.emit(it, type) } }) {
            Text(
                modifier = modifier.padding(16.dp),
                text = stringResource(type.resId),
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(
    fontScale = 1f,
    name = "PHONE - Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Preview(
    fontScale = 1f,
    name = "TABLET - Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
    device = Devices.TABLET
)
@Preview(
    fontScale = 1f,
    name = "PHONE - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Preview(
    fontScale = 1f,
    name = "TABLET - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true,
    device = Devices.TABLET
)
@Composable
fun LaunchPreview() {
    val modifier = Modifier
    val appThemeState = DefaultAppThemeState(
        isDarkTheme = false,
        colorPalette = ColorPalette.Coral
    )
    BioMapTheme(
        systemUiController = null,
        appThemeState = appThemeState,
    ) {
        TopView(
            modifier = modifier,
            context = null,
            viewModel = DefaultMainViewModel()
        )
    }
}