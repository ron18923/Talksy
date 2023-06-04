package com.example.talksy.presentation.chatFrame.settings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.user.UserRepository
import com.example.talksy.data.user.UserStateListener
import com.example.talksy.presentation.chatFrame.ChatFrameEvent
import com.example.talksy.presentation.chatFrame.ChatFrameStates
import com.example.talksy.presentation.login.LoginEvent
import com.example.talksy.presentation.login.LoginStates
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(SettingsStates())
    val state: State<SettingsStates> = _state

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    private val user = userRepository.getUser()

    private val userStateListener = UserStateListenerImpl()

    init {
        if (user != null) _state.value = _state.value.copy(
            displayName = user.displayName!!,
            email = user.email!!
        )
        userRepository.setListener(userStateListener)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SignOut -> {
                userRepository.signOut()
            }
        }
    }

    inner class UserStateListenerImpl: UserStateListener{
        override fun onUserStateChanged() {
            val updatedUser = userRepository.getUser()
            if (updatedUser != null) _state.value = _state.value.copy(
                displayName = updatedUser.displayName!!,
                email = updatedUser.email!!
            )
            if(updatedUser == null){
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.SignOut
                    )
                }
            }
        }

    }
}

data class SettingsViewModelContainer(
    var state: SettingsStates,
    var onEvent: (SettingsEvent) -> Unit,
    var events: SharedFlow<SettingsEvent>,
)