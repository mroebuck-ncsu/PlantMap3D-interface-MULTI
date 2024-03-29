package edu.ncsu.biomap.textinput

import android.content.res.Configuration
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import edu.ncsu.biomap.ui.theme.Checkmark
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.ncsu.biomap.R
import edu.ncsu.biomap.dashboard.DashboardKeys
import edu.ncsu.biomap.dashboard.TopAppBarActionType
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.ui.common.ColorPalette
import edu.ncsu.biomap.ui.common.DefaultAppActivity
import edu.ncsu.biomap.ui.common.DefaultAppThemeState
import edu.ncsu.biomap.ui.theme.BioMapTheme
import kotlinx.coroutines.launch

class TextInputActivity : DefaultAppActivity() {

    // region Properties

    private lateinit var viewModel: TextInputViewModel

    // endregion

    // region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = DefaultTextInputViewModel()
        viewModel.onRestoreInstanceState(savedInstanceState ?: intent.extras)
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun MyApp(savedInstanceState: Bundle?) {
        ConfigViewModel()
        TopView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }
    // endregion

    // region Private Composable Methods

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun ConfigViewModel() {
        val currentState: MutableState<TextInputStore.State> =
            remember { mutableStateOf(TextInputStore.State.Idle) }
        val focusManager = LocalFocusManager.current
        val focusRequester = FocusRequester()
        val shouldShowError = rememberSaveable { mutableStateOf(false) }
        val value = rememberSaveable { mutableStateOf(viewModel.initialValue) }
        val keyboardController = LocalSoftwareKeyboardController.current

        viewModel.setUp(
            currentState = currentState,
            focusManager = focusManager,
            focusRequester = focusRequester,
            shouldShowError = shouldShowError,
            value = value,
            keyboardController = keyboardController,
        )

        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                // repeatOnLifecycle launches the block in a new coroutine every time the
                // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Trigger the flow and start listening for values.
                    // Note that this happens when lifecycle is STARTED and stops
                    // collecting when the lifecycle is STOPPED
                    viewModel.stateFlow.collect { value -> onStateReceived(value) }
                }
            }
        }
    }

    @Composable
    private fun TopView(modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colors.background,
        ) {
            Scaffold(
                snackbarHost = { /*CreateSnackBarHost(it)*/ },
                topBar = { CreateTopAppBar(modifier) },
                drawerGesturesEnabled = true,
                isFloatingActionButtonDocked = true,
            ) {
                ScaffoldContentView(modifier, it)
            }
        }
    }

    @Composable
    private fun CreateTopAppBar(modifier: Modifier) {
        TopAppBar(
            modifier = modifier,
            title = { Text(
                viewModel.attribute.value?.attribute ?: "Attribute",
                modifier = modifier
            ) },
            actions = {
                // options icon (vertical dots)
                TopAppBarActionButton(
                    modifier = modifier,
                    text = "Save"
                ) {
                    onSaveRequested()
                }
            }
        )
    }

    @Composable
    private fun TopAppBarActionButton(
        modifier: Modifier,
        text: String,
        onClick: (Int) -> Unit
    ) {
        viewModel.apply {
            val color = if(this.canSaveChanges) {
                MaterialTheme.colors.onBackground
            } else {
                Color.Gray
            }

            val builder = AnnotatedString.Builder()
            builder.withStyle(style = SpanStyle(color = color)) {
                append(text)
            }
            val annotatedString = builder.toAnnotatedString()
            ClickableText(
                text = annotatedString,
                modifier = modifier
                    .padding(4.dp, 0.dp, 16.dp, 0.dp),
                onClick = onClick,
            )
        }
    }

    @Composable
    private fun ScaffoldContentView(modifier: Modifier, it: PaddingValues) {
        Column(
            modifier = modifier
                .idealWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                modifier = modifier.padding(16.dp),
                text = "Placeholder Title",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )
            CreateTextField(
                modifier = modifier,
                viewModel = viewModel,
            )
            Divider(color = Color.LightGray)
            CreateAdditionalOptions(
                modifier = modifier,
                viewModel = viewModel,
            )
        }
    }

    @Composable
    private fun CreateAdditionalOptions(modifier: Modifier, viewModel: TextInputViewModel) {
        viewModel.apply {
            val list = attribute.value?.options ?: listOf()
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = list,
                    itemContent = {
                        CreateNextAdditionalOption(
                            modifier,
                            it
                        )
                    }
                )
            }
        }
    }

    @Composable
    private fun CreateNextAdditionalOption(modifier: Modifier, text: String) {
        Column(
            modifier
                .fillMaxWidth()
                .clickable {
                    onOptionClicked(text)
                }) {
            Row(modifier = modifier) {
                Text(
                    modifier = modifier.padding(16.dp, 0.dp, 16.dp, 8.dp),
                    text = text,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start,
                )
                if(viewModel.attribute.value?.value == text) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Checkmark
                    )
                }
            }
        }
    }
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun CreateTextField(
        modifier: Modifier,
        viewModel: TextInputViewModel
    ) {
        viewModel.apply {
            val text = attribute.value?.value ?: ""
            val textFieldValue = remember {
                mutableStateOf(
                    TextFieldValue(
                        text = text,
                        selection = TextRange(if(text.isEmpty()) 0 else text.length - 1)
                    )
                )
            }
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = text,
                maxLines = 1,
                onValueChange = {
                    onValueChanged(it)
                    textFieldValue.value = TextFieldValue(
                        text = it,
                        selection = TextRange(it.length)
                    )
                },
                placeholder = {
                    Text(text = "Placeholder Value")
                },
                label = { Text("Placeholder Label") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSaveRequested()
                    }
                ),
                singleLine = true,
                isError = shouldShowError.value,
                visualTransformation = VisualTransformation.None,
                colors = outlinedTextFieldColors()
            )
            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
                onDispose { }
            }
        }
    }

    @Composable
    private fun CreateCircularProgressIndicator(
        modifier: Modifier = Modifier,
        progress: Float = 0.0f,
        color: Color = MaterialTheme.colors.primary,
        strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val progressAnimationValue by infiniteTransition.animateFloat(
            initialValue = progress,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(animation = tween(900))
        )

        CircularProgressIndicator(
            modifier = modifier,
            progress = progressAnimationValue,
            color = color,
            strokeWidth = strokeWidth
        )
    }

    // endregion

    // region Private Non-Composable Methods

    private fun onOptionClicked(text: String) {
        viewModel.emit(TextInputStore.Intent.ChangeRequested(text))
    }

    private fun onValueChanged(text: String) {
        viewModel.emit(TextInputStore.Intent.ChangeRequested(text))
    }

    private fun onStateReceived(state: TextInputStore.State) {
        println("${this::class.simpleName}: New State Received=[$state]")
        when (state) {
            is TextInputStore.State.ApiRequestError -> {}
            TextInputStore.State.NewTextInput -> {}
            TextInputStore.State.ApiRequestInProgress -> {}
            is TextInputStore.State.ApiRequestSuccess -> onApiRequestSuccess(state)
            TextInputStore.State.SaveCancelled -> {}
            is TextInputStore.State.InputError -> {}
            TextInputStore.State.Idle -> {}
        }
    }

    private fun onApiRequestSuccess(state: TextInputStore.State.ApiRequestSuccess) {
        viewModel.emit(TextInputStore.Intent.Finish(this))
    }

    private fun onSaveRequested() {
        viewModel.emit(TextInputStore.Intent.SaveRequested)
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
    fun LaunchPreview() {
        val appThemeState = DefaultAppThemeState(
            isDarkTheme = false,
            colorPalette = ColorPalette.Coral
        )
        BioMapTheme(
            systemUiController = null,
            appThemeState = appThemeState,
        ) {
            val attributeModel = AttributeModel("Plot ID", "5th", listOf(
                "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
            ))
            val savedInstanceState = Bundle()
            savedInstanceState.putSerializable(DashboardKeys.Attribute.name, attributeModel)

            viewModel = DefaultTextInputViewModel()
            viewModel.onRestoreInstanceState(savedInstanceState)
            ConfigViewModel()
            TopView()
        }
    }

    // endregion
}

private fun Modifier.idealWidth(): Modifier = composed {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp

    when {
        width < 600 -> fillMaxWidth()
        else -> requiredSizeIn(maxWidth = 600.dp)
    }
}
