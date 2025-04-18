package io.tanlnm.gitfetcher.core.platform

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MVIViewModel<C : ICommand, S : IState, E : IEffect> : ViewModel() {
    protected lateinit var ioScope: CoroutineScope

    private val _state = MutableStateFlow<IState>(InitialState)
    val state: Flow<IState> = _state.asStateFlow()

    private val _command = MutableSharedFlow<ICommand>()
    private val command: Flow<ICommand> = _command.asSharedFlow()

    private val _effect = Channel<IEffect>()
    val effect: Flow<IEffect> = _effect.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ioScope = this
            command.collect { handleCommand(it) }
        }
    }

    // region Command
    @WorkerThread
    abstract suspend fun handleCommand(command: ICommand)

    fun sendCommand(command: ICommand) {
        viewModelScope.launch(Dispatchers.IO) { _command.emit(command) }
    }
    // endregion

    // region State
    @WorkerThread
    protected suspend fun setState(newState: IState) = _state.emit(newState)

    @WorkerThread
    protected fun updateState(newState: IState) = _state.update { newState }
    // endregion

    // region Effect
    @WorkerThread
    protected suspend fun setEffect(effect: IEffect) = _effect.send(effect)
    // endregion
}