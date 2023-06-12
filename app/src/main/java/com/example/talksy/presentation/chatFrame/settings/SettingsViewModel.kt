package com.example.talksy.presentation.chatFrame.settings

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.UserRepository
import com.example.talksy.data.UserStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _user = userRepository.getUser()

    private val _userStateListener = UserStateListenerImpl()

    init {
        if (_user != null) _state.value = _state.value.copy(
            username = _user.displayName ?: "",
            email = _user.email ?: "",
            profilePicture = _user.photoUrl ?: Uri.EMPTY
        )
        userRepository.setListener(_userStateListener)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SignOut -> userRepository.signOut()
            SettingsEvent.GoToEditProfile -> {
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.GoToEditProfile
                    )
                }
            }
        }
    }

    inner class UserStateListenerImpl: UserStateListener{
        override fun onUserStateChanged() {
            val updatedUser = userRepository.getUser()
            if (updatedUser != null) _state.value = _state.value.copy(
                username = updatedUser.displayName?:"",
                email = updatedUser.email?:""
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