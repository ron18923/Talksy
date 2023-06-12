package com.example.talksy.presentation.startCompose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.UserRepository
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

    private var _user = userRepository.getUser()

    init {
       _state.value = _state.value.copy(isSignedIn = isSignedIn())
    }

    private fun isSignedIn(): Boolean {
        _user = userRepository.getUser()
        return _user != null
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