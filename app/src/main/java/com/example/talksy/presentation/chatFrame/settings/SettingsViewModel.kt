package com.example.talksy.presentation.chatFrame.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.chatFrame.ChatFrameEvent
import com.example.talksy.presentation.chatFrame.ChatFrameStates
import com.example.talksy.presentation.login.LoginEvent
import com.example.talksy.presentation.login.LoginStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(SettingsStates())
    val state: State<SettingsStates> = _state

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    val user = userRepository.getUser()!!

    init {
        _state.value = _state.value.copy(
            displayName = user.displayName!!,
            email = user.email!!
        )
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.TempEvent -> {}
        }
    }
}

data class SettingsViewModelContainer(
    var state: SettingsStates,
    var onEvent: (SettingsEvent) -> Unit,
    var events: SharedFlow<SettingsEvent>,
)