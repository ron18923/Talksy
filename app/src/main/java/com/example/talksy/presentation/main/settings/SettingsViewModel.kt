package com.example.talksy.presentation.main.settings

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _user = mainRepository.getUser()

    private val _userStateListener = UserStateListenerImpl()

    init {
        if (_user != null) Log.d(TAG, "user: ${_user.displayName}")
        if (_user != null) _state.value = _state.value.copy(
            username = _user.displayName ?: "",
            email = _user.email ?: "",
            profilePicture = _user.photoUrl ?: Uri.EMPTY
        )
        mainRepository.setUserListener(_userStateListener)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SignOut -> {
                Log.d(TAG, "onEvent: event")
                mainRepository.signOutUser()
            }

            SettingsEvent.GoToEditProfile -> {
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.GoToEditProfile
                    )
                }
            }
        }
    }

    inner class UserStateListenerImpl : UserStateListener {
        override fun onUserStateChanged() {
            val updatedUser = mainRepository.getUser()
            if (updatedUser != null) _state.value = _state.value.copy(
                username = updatedUser.displayName ?: "",
                email = updatedUser.email ?: ""
            )
            if (updatedUser == null) {
                viewModelScope.launch {
                    _events.emit(
                        SettingsEvent.SignOut
                    )
                }
            }
        }

    }
}