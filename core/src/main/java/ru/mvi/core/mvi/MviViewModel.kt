package ru.mvi.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface MviAction
interface MviState
interface MviEffect
interface MviEvent

abstract class MviViewModel<
        ACTION : MviAction,
        STATE : MviState,
        EFFECT : MviEffect,
        EVENT : MviEvent> : ViewModel() {

    protected val events = MutableSharedFlow<EVENT>(replay = 1, extraBufferCapacity = 5)

    private val actions = Channel<ACTION>(capacity = 10)
    private val _effect = Channel<EFFECT>()
    val effect = _effect.receiveAsFlow()

    val state: StateFlow<STATE> = actions
        .receiveAsFlow()
        .runningFold(createInitState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, createInitState())

    abstract fun reduceState(state: STATE, action: ACTION): STATE
    abstract fun createInitState(): STATE

    fun setEvent(event: EVENT) = events.tryEmit(event)

    protected fun setAction(action: ACTION) = actions.trySend(action)
    protected fun setEffect(builder: () -> EFFECT) = this._effect.trySend(builder())
}
