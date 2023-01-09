package edu.ncsu.biomap.textinput

import android.app.Activity

sealed class TextInputStore {
    sealed class Intent {
        data class ChangeRequested(val value: String) : Intent()
        object SaveRequested : Intent()
        data class Finish(val activity: Activity) : Intent()
    }

    sealed class State {
        object Idle : State()
        object NewTextInput : State()
        data class InputError(val throwable: Throwable) : State()
        object SaveCancelled : State()
        object ApiRequestInProgress : State()
        object ApiRequestSuccess : State()
        data class ApiRequestError(val throwable: Throwable) : State()
    }
}
