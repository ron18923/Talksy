package com.example.talksy.presentation.main.settings

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(SettingsStates())
    val state: State<SettingsStates> = _state

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()

    private val _userStateListener = UserStateListenerImpl()

    init {
        init()
        mainRepository.setUserListener(_userStateListener)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SignOut -> {
                mainRepository.signOutUser()
            }

            SettingsEvent.GoToEditProfile -> {
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.GoToEditProfile
                    )
                }
            }

            SettingsEvent.Dispose -> _state.value =
                _state.value.copy(username = "", email = "", profilePicture = Uri.EMPTY)
        }
    }

    private fun init() {
        viewModelScope.launch {
            mainRepository.getUser { user ->
                if (user == null) return@getUser
                _state.value = _state.value.copy(
                    username = user.username,
                    email = user.email,
                    profilePicture = Uri.parse(user.profilePicture)
                )
            }
        }
    }

    inner class UserStateListenerImpl : UserStateListener {
        override fun onUserStateChanged() {
            _state.value = _state.value.copy(username = "", email = "", profilePicture = Uri.EMPTY)
            init()
            if (!mainRepository.isUserLoggedIn()) {
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.SignOut
                    )
                }
            }
        }
    }
}