package edu.ncsu.biomap.dashboard

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.ui.common.ColorPalette
import edu.ncsu.biomap.ui.common.DefaultAppActivity
import edu.ncsu.biomap.ui.common.DefaultAppThemeState
import edu.ncsu.biomap.ui.theme.BioMapTheme
import edu.ncsu.biomap.util.serializable
import kotlinx.coroutines.launch

class DashboardActivity : DefaultAppActivity() {

    // region Properties

    private lateinit var viewModel: MutableState<DashboardViewModel>
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var dropDownMenuExpanded: MutableState<Boolean>
    private lateinit var selectedBottomNavigationItem: MutableState<Int>
    private lateinit var navController: NavHostController
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val extras = result.data?.extras
                val attributeModel: AttributeModel? = extras?.serializable(
                    DashboardKeys.Attribute.name,
                    AttributeModel::class.java
                ) as AttributeModel?
                viewModel.value.updateAttribute(attributeModel)
            }
        }

    // endregion

    // region Override Methods

    @Composable
    override fun MyApp(savedInstanceState: Bundle?) {
        InitViewModel()
        TopView()
    }

    // endregion

    // region Private Composable Methods

    @Composable
    private fun InitViewModel() {
        val navigator = DefaultDashboardNavigator(startForResult = startForResult)
        val attributes = rememberSaveable {
            mutableStateOf(listOf<AttributeModel>())
        }
        val cameras = rememberSaveable {
            mutableStateOf(listOf<String>())
        }
        val bottomBarItems = rememberSaveable {
            mutableStateOf(listOf<DashboardBottomItemType>())
        }
        val currentBottomBarItem = rememberSaveable {
            mutableStateOf(DashboardBottomItemType.Configuration)
        }
        val currentDataCollectionIntent = rememberSaveable {
            mutableStateOf<TopAppBarActionType.DataCollection?>(null)
        }

        viewModel = remember {
            mutableStateOf(
                DefaultDashboardViewModel(
                    navigator = navigator,
                    attributes = attributes,
                    cameras = cameras,
                    bottomBarItems = bottomBarItems,
                    currentBottomBarItem = currentBottomBarItem,
                    currentDataCollectionIntent = currentDataCollectionIntent,
                )
            )
        }
        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                // repeatOnLifecycle launches the block in a new coroutine every time the
                // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Trigger the flow and start listening for values.
                    // Note that this happens when lifecycle is STARTED and stops
                    // collecting when the lifecycle is STOPPED
                    viewModel.value.stateFlow.collect { value -> onStateReceived(value) }
                }
            }
        }
    }

    @Composable
    private fun TopView(modifier: Modifier = Modifier) {
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
                topBar = { CreateTopAppBar(modifier) },
                bottomBar = { CreateBottomBar(modifier) },
                drawerContent = { CreateDrawerContent(modifier) },
                floatingActionButton = { /*BuildFloatingActionButton()*/ },
                floatingActionButtonPosition = FabPosition.Center,
                drawerGesturesEnabled = true,
                isFloatingActionButtonDocked = true,
            ) {
                ScaffoldContentView(modifier, it)
            }
        }
    }

    @Composable
    private fun CreateTopAppBar(modifier: Modifier) {
        viewModel.value.apply {
            currentBottomBarItem.value.apply {
                when(this) {
                    DashboardBottomItemType.Configuration ->
                        CreateConfigurationTopAppBar(modifier, this)
                    DashboardBottomItemType.DataCollection ->
                        CreateDataCollectionTopAppBar(modifier, this)
                    DashboardBottomItemType.DataReview ->
                        CreateDataReviewTopAppBar(modifier, this)
                }
            }
        }
    }

    @Composable
    private fun CreateConfigurationTopAppBar(
        modifier: Modifier,
        item: DashboardBottomItemType
    ) {
        TopAppBar(
            modifier = modifier,
            title = { Text(item.title) },
        )
    }

    @Composable
    private fun CreateDataCollectionTopAppBar(
        modifier: Modifier,
        item: DashboardBottomItemType
    ) {
        TopAppBar(
            modifier = modifier,
            title = { Text(item.title) },
            actions = {
                // options icon (vertical dots)
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.MoreVert,
                    description = "Options"
                ) {
                    // show the drop down menu
                    dropDownMenuExpanded.value = true
                }


                CreateDropDownMenu(modifier, TopAppBarActionType.DataCollection.values().toList())
            }
        )
    }

    @Composable
    private fun CreateDataReviewTopAppBar(
        modifier: Modifier,
        item: DashboardBottomItemType
    ) {
        TopAppBar(
            modifier = modifier,
            title = { Text(item.title) },
            actions = {
                // options icon (vertical dots)
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.MoreVert,
                    description = "Options"
                ) {
                    // show the drop down menu
                    dropDownMenuExpanded.value = true
                }

                CreateDropDownMenu(modifier, TopAppBarActionType.DataReview.values().toList())
            }
        )
    }

    @Composable
    private fun CreateDropDownMenu(
        modifier: Modifier,
        list: List<TopAppBarActionType>) {
        DropdownMenu(
            modifier = modifier,
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
            list.map {
                DropdownMenuItem(onClick = {
                    showToast("${it.title} Click")
                    dropDownMenuExpanded.value = false
                }) {
                    Text(it.title)
                }
            }
        }
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
        BottomNavigation(modifier = modifier) {
            viewModel.value.bottomBarItems.value.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(item.icon, contentDescription = item.contentDescription)
                    },
                    label = { Text(text = item.title) },
                    alwaysShowLabel = true,
                    selected = selectedBottomNavigationItem.value == viewModel.value.bottomBarItems.value.indexOf(item),
                    onClick = {
                        selectedBottomNavigationItem.value = viewModel.value.bottomBarItems.value.indexOf(item)
                        viewModel.value.emit(DashboardStore.Intent.BottomBarItemSelected(item))
                    }
                )
            }
        }
    }

    @Composable
    private fun CreateDrawerContent(modifier: Modifier) {
        Text(
            "Drawer title",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        )
        Divider()

        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = viewModel.value.cameras.value,
                itemContent = {
                    CreateDrawerContentItem(
                        modifier,
                        it
                    )
                }
            )
        }
    }

    @Composable
    private fun CreateDrawerContentItem(modifier: Modifier, text: String) {
        Column(
            modifier
                .fillMaxWidth()
                .clickable {
                    onCameraSelected(text)
                }) {
            Text(
                text,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )
            Divider()
        }
    }

    @Composable
    private fun ScaffoldContentView(
        modifier: Modifier,
        paddingValues: PaddingValues
    ) {
        viewModel.value.apply {
            currentBottomBarItem.value.apply {
                when(this) {
                    DashboardBottomItemType.Configuration ->
                        CreateConfigurationContentView(modifier)
                    DashboardBottomItemType.DataCollection ->
                        CreateDataCollectionContentView(modifier)
                    DashboardBottomItemType.DataReview ->
                        CreateDataReviewContentView(modifier)
                }
            }
        }
    }

    @Composable
    private fun CreateConfigurationContentView(modifier: Modifier) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = viewModel.value.attributes.value,
                itemContent = {
                    CreateConfigurationAttribute(
                        modifier,
                        it
                    )
                }
            )
        }
    }

    @Composable
    private fun CreateDataCollectionContentView(modifier: Modifier) {

    }

    @Composable
    private fun CreateDataReviewContentView(modifier: Modifier) {

    }

    @Composable
    private fun CreateConfigurationAttribute(
        modifier: Modifier,
        attributeModel: AttributeModel
    ) {
        val context = LocalContext.current.applicationContext

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
                    .clickable {
                        onColumnClicked(attributeModel)
                    }) {
                Text(
                    modifier = modifier.padding(16.dp, 4.dp, 16.dp, 4.dp),
                    text = attributeModel.attribute,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = modifier.padding(16.dp, 4.dp, 16.dp, 8.dp),
                    text = attributeModel.value,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }

    // endregion

    // region Private Non-Composable Methods

    private fun onColumnClicked(attributeModel: AttributeModel) {
        viewModel.value.emit(
            DashboardStore.Intent.AttributeSelected(
                this,
                attributeModel
            )
        )
    }

    private fun onCameraSelected(text: String) {
        showToast("$text Clicked")
    }

    private fun onStateReceived(state: DashboardStore.State) {
        println("${this::class.simpleName}: New State Received=[$state]")
        when (state) {
            is DashboardStore.State.AttributeUpdated -> {}
            DashboardStore.State.Idle -> {}
        }
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }

    // endregion

    // region Previews

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
    fun LaunchConfigurationPreview() {
        LaunchPreviewWithBottomBarItem(DashboardBottomItemType.Configuration)
    }

    @Preview(
        fontScale = 1f,
        name = "DataCollection - Phone",
        showSystemUi = true,
        showBackground = true,
        device = Devices.TABLET
    )
    @Preview(
        fontScale = 1f,
        name = "DataCollection - Tablet",
        showSystemUi = true,
        showBackground = true,
        device = Devices.PHONE
    )
    @Composable
    fun LaunchDataCollectionPreview() {
        LaunchPreviewWithBottomBarItem(DashboardBottomItemType.DataCollection)
    }

    @Preview(
        fontScale = 1f,
        name = "DataReview - Phone",
        showSystemUi = true,
        showBackground = true,
        device = Devices.TABLET
    )
    @Preview(
        fontScale = 1f,
        name = "DataReview - Tablet",
        showSystemUi = true,
        showBackground = true,
        device = Devices.PHONE
    )
    @Composable
    fun LaunchDataReviewPreview() {
        LaunchPreviewWithBottomBarItem(DashboardBottomItemType.DataReview)
    }

    @Composable
    fun LaunchPreviewWithBottomBarItem(configuration: DashboardBottomItemType) {
        val modifier = Modifier
        val appThemeState = DefaultAppThemeState(
            isDarkTheme = false,
            colorPalette = ColorPalette.Coral
        )
        val attributes = rememberSaveable { mutableStateOf(listOf<AttributeModel>()) }
        val cameras = rememberSaveable { mutableStateOf(listOf<String>()) }
        val bottomBarItems = rememberSaveable { mutableStateOf(listOf<DashboardBottomItemType>()) }
        val currentBottomBarItem = rememberSaveable { mutableStateOf(configuration) }
        val currentDataCollectionIntent = rememberSaveable {
            mutableStateOf<TopAppBarActionType.DataCollection?>(null)
        }
        val fakeViewModel = DefaultDashboardViewModel(
            navigator = FakeDashboardNavigator(),
            cameras = cameras,
            attributes = attributes,
            bottomBarItems = bottomBarItems,
            currentBottomBarItem = currentBottomBarItem,
            currentDataCollectionIntent = currentDataCollectionIntent,
        )

        viewModel = remember {
            mutableStateOf(fakeViewModel)
        }
        BioMapTheme(
            systemUiController = null,
            appThemeState = appThemeState,
        ) {
            TopView(modifier)
        }
    }

    // endregion
}
