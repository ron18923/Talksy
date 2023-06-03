package com.example.talksy.presentation.startCompose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(NavigationStates())
    val state: State<NavigationStates> = _state

    private val _events = MutableSharedFlow<NavigationEvent>()
    val events = _events.asSharedFlow()

    var user = userRepository.getUser()

    init {
       _state.value = _state.value.copy(isSignedIn = isSignedIn())
    }

    private fun isSignedIn(): Boolean {
        user = userRepository.getUser()
        return user != null
    }

    fun onEvent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.CheckSignedIn -> {
                _state.value = _state.value.copy(isSignedIn = isSignedIn())
            }
        }
    }
}

sealed class NavigationEvent {
    object CheckSignedIn : NavigationEvent()
}

data class NavigationStates(
    var isSignedIn: Boolean = false
)