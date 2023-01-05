package edu.ncsu.biomap.dashboard

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.ncsu.biomap.R
import edu.ncsu.biomap.ui.common.ColorPalette
import edu.ncsu.biomap.ui.common.DefaultAppActivity
import edu.ncsu.biomap.ui.common.DefaultAppThemeState
import edu.ncsu.biomap.ui.theme.Bamboo3
import edu.ncsu.biomap.ui.theme.BioMapTheme
import kotlinx.coroutines.launch

class DashboardActivity : DefaultAppActivity() {

    private lateinit var viewModel: MutableState<DashboardViewModel>
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var dropDownMenuExpanded: MutableState<Boolean>
    private lateinit var selectedBottomNavigationItem: MutableState<Int>
    private lateinit var navController: NavHostController

    @Composable
    override fun MyApp(savedInstanceState: Bundle?) {
        InitViewModel()
        TopView(viewModel = viewModel.value,)
    }

    // region Private Composable Methods

    @Composable
    private fun InitViewModel() {
        viewModel = remember {
            mutableStateOf(DefaultDashboardViewModel())
        }
    }

    @Composable
    private fun TopView(
        modifier: Modifier = Modifier,
        viewModel: DashboardViewModel,
    ) {
        val context = LocalContext.current.applicationContext

        scaffoldState = rememberScaffoldState()
        selectedBottomNavigationItem = remember { mutableStateOf(0) }
        dropDownMenuExpanded = remember { mutableStateOf(false) }
        navController = rememberNavController()

        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = { /*CreateSnackBarHost(it)*/ },
                topBar = { CreateTopAppBar() },
                bottomBar = { CreateBottomBar(modifier) },
                drawerContent = { CreateDrawerContent() },
                floatingActionButton = { /*BuildFloatingActionButton()*/ },
                floatingActionButtonPosition = FabPosition.Center,
                drawerGesturesEnabled = true,
                isFloatingActionButtonDocked = true,
            ) {
                println("it=[$it]")
                ScaffoldContentView(modifier, context, it)
            }
        }
    }

    @Composable
    private fun CreateTopAppBar() {
        val context = LocalContext.current.applicationContext

        TopAppBar(
            title = { Text("Dashboard") },
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Navigation icon")
                }
            },
            actions = {
                // search icon
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.Search,
                    description = "Search"
                ) {
                    showToast("Search Click")
                }

                // lock icon
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.Lock,
                    description = "Lock"
                ) {
                    showToast("Lock Click")
                }

                // options icon (vertical dots)
                TopAppBarActionButton(imageVector = Icons.Outlined.MoreVert, description = "Options") {
                    // show the drop down menu
                    dropDownMenuExpanded.value = true
                }

                // drop down menu
                DropdownMenu(
                    expanded = dropDownMenuExpanded.value,
                    onDismissRequest = {
                        dropDownMenuExpanded.value = false
                    },
                    // play around with these values
                    // to position the menu properly
                    offset = DpOffset(x = 10.dp, y = (-60).dp)
                ) {
                    // this is a column scope
                    // items are added vertically

                    DropdownMenuItem(onClick = {
                        showToast("Refresh Click")
                        dropDownMenuExpanded.value = false
                    }) {
                        Text("Refresh")
                    }

                    DropdownMenuItem(onClick = {
                        showToast("Settings Click")
                        dropDownMenuExpanded.value = false
                    }) {
                        Text("Settings")
                    }

                    DropdownMenuItem(onClick = {
                        showToast("Send Feedback Click")
                        dropDownMenuExpanded.value = false
                    }) {
                        Text("Send Feedback")
                    }
                }
            }
        )
    }

    @Composable
    private fun TopAppBarActionButton(
        imageVector: ImageVector,
        description: String,
        onClick: () -> Unit
    ) {
        IconButton(onClick = {
            onClick()
        }) {
            Icon(imageVector = imageVector, contentDescription = description)
        }
    }

    @Composable
    private fun CreateBottomBar(modifier: Modifier) {
        val items = listOf(
            "Configuration",
            "Data Collection",
            "Data Review",
        )
        BottomNavigation(modifier = modifier) {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    },
                    label = { Text(text = item) },
                    alwaysShowLabel = true,
                    selected = selectedBottomNavigationItem.value == items.indexOf(item),
                    onClick = {
                        selectedBottomNavigationItem.value = items.indexOf(item)
                        showToast("$item Click")
                    }
                )
            }
        }
    }

    @Composable
    private fun CreateDrawerContent() {
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
    }

    @Composable
    private fun ScaffoldContentView(
        modifier: Modifier,
        context: Context?,
        paddingValues: PaddingValues
    ) {
        val list = listOf("Affiliation", "Weeds/Cover Crop", "Timing", "Plot ID", "Weather")

        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = list,
                itemContent = {
                    CreateConfigurationAttribute(
                        modifier,
                        context,
                        it,
                        "None"
                    )
                }
            )
        }
    }

    @Composable
    private fun CreateConfigurationAttribute(
        modifier: Modifier,
        context: Context?,
        text: String,
        selectionText: String,
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .clickable { }) {
                Text(
                    modifier = modifier.padding(16.dp, 4.dp, 16.dp, 4.dp),
                    text = text,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = modifier.padding(16.dp, 4.dp, 16.dp, 8.dp),
                    text = selectionText,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }

    // endregion

    // region Private Non-Composable Methods

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }

    // endregion

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
                viewModel = DefaultDashboardViewModel()
            )
        }
    }
}
