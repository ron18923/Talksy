package com.example.talksy.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _state = mutableStateOf(MainStates())
    val state: State<MainStates> = _state

    private val _events = MutableSharedFlow<MainEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.NoUserFound -> {
                viewModelScope.launch {
                    _events.emit(
                        MainEvent.NoUserFound
                    )
                }
            }

            is MainEvent.NavItemSelected -> {
                _state.value = _state.value.copy(selectedNavItem = event.value)
            }
        }
    }
}