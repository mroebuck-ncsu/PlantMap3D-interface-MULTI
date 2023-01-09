package edu.ncsu.biomap.textinput

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import edu.ncsu.biomap.dashboard.DashboardKeys
import edu.ncsu.biomap.model.AttributeModel
import edu.ncsu.biomap.util.serializable
import edu.ncsu.log.DefaultLogAdapter
import edu.ncsu.log.LogAdapter
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultTextInputViewModel(
    private val logAdapter: LogAdapter = DefaultLogAdapter(true)
) : ViewModel(), TextInputViewModel {

    // region Properties
    override var initialValue: AttributeModel? = null
    override lateinit var currentState: MutableState<TextInputStore.State>
    override lateinit var focusManager: FocusManager
    override lateinit var focusRequester: FocusRequester
    override lateinit var shouldShowError: MutableState<Boolean>
    @ExperimentalComposeUiApi
    override var keyboardController: SoftwareKeyboardController? = null

    override var attribute: MutableState<AttributeModel?> = mutableStateOf(null)

    private val _stateFlow: MutableStateFlow<TextInputStore.State> =
        MutableStateFlow(TextInputStore.State.Idle)
    override val stateFlow: MutableStateFlow<TextInputStore.State> = _stateFlow

    // endregion

    // region Override Methods

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            initialValue = it.serializable(
                key = DashboardKeys.Attribute.name,
                clazz = AttributeModel::class.java
            ) as AttributeModel?
            logAdapter.i("attribute.value=[${attribute.value}]")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(DashboardKeys.Attribute.name, attribute.value)
    }

    override fun emit(intent: TextInputStore.Intent) {
        println("${this::class.simpleName}: New Intent Received=[$intent]")
        when(intent) {
            is TextInputStore.Intent.ChangeRequested -> onChangeRequested(intent)
            TextInputStore.Intent.SaveRequested -> onSaveRequested(intent)
            is TextInputStore.Intent.Finish -> onFinish(intent)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun setUp(
        currentState: MutableState<TextInputStore.State>,
        focusManager: FocusManager,
        focusRequester: FocusRequester,
        shouldShowError: MutableState<Boolean>,
        value: MutableState<AttributeModel?>,
        keyboardController: SoftwareKeyboardController?,
    ) {
        this.currentState = currentState
        this.focusManager = focusManager
        this.focusRequester = focusRequester
        this.shouldShowError = shouldShowError
        this.attribute = value
        this.keyboardController = keyboardController
    }

    private fun onFinish(intent: TextInputStore.Intent.Finish) {
        val data = Intent()
        val bundle = Bundle()
        bundle.putSerializable(DashboardKeys.Attribute.name, attribute.value)
        data.putExtras(bundle)
        intent.activity.apply {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    // endregion

    // region Private Methods

    private fun onChangeRequested(intent: TextInputStore.Intent.ChangeRequested) {
        attribute.value = attribute.value?.copy(value = intent.value)
        _stateFlow.value = TextInputStore.State.NewTextInput
    }

    private fun onSaveRequested(intent: TextInputStore.Intent) {
        _stateFlow.value = TextInputStore.State.ApiRequestSuccess
    }

    // endregion
}