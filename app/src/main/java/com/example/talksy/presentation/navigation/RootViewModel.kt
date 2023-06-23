package com.example.talksy.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.presentation.main.settings.SettingsEvent
import com.example.talksy.presentation.main.settings.SettingsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(RootStates())
    val state: State<RootStates> = _state

    private val _events = MutableSharedFlow<RootEvent>()
    val events = _events.asSharedFlow()

    init {
        _state.value = _state.value.copy(isUserLoggedIn = (mainRepository.getUser() != null))
    }
}

sealed class RootEvent {
}

data class RootStates(
    var isUserLoggedIn : Boolean = false
)