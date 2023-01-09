package edu.ncsu.biomap.textinput

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import edu.ncsu.biomap.model.AttributeModel
import kotlinx.coroutines.flow.MutableStateFlow

interface TextInputViewModel {
    fun onRestoreInstanceState(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle?)
    fun emit(intent: TextInputStore.Intent)
    @OptIn(ExperimentalComposeUiApi::class)
    fun setUp(
        currentState: MutableState<TextInputStore.State>,
        focusManager: FocusManager,
        focusRequester: FocusRequester,
        shouldShowError: MutableState<Boolean>,
        value: MutableState<AttributeModel?>,
        keyboardController: SoftwareKeyboardController?,
    )

    val initialValue: AttributeModel?
    val currentState: MutableState<TextInputStore.State>
    val focusManager: FocusManager
    val focusRequester: FocusRequester
    val shouldShowError: MutableState<Boolean>
    val attribute: MutableState<AttributeModel?>
    @OptIn(ExperimentalComposeUiApi::class)
    val keyboardController: SoftwareKeyboardController?
    val stateFlow: MutableStateFlow<TextInputStore.State>
}
