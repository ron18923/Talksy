package com.example.talksy.presentation.editProfile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _state = mutableStateOf(EditProfileStates())
    val state: State<EditProfileStates> = _state

    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events = _events.asSharedFlow()

    private val _user = userRepository.getUser()

    init {
        updateScreenValues()
    }

    private fun updateScreenValues() {
        if (_user != null) _state.value = _state.value.copy(
            email = _user.email ?: "",
            username = _user.displayName ?: ""
        )
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            EditProfileEvent.GoBackClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.GoBackClicked
                    )
                }
            }
            is EditProfileEvent.EmailChanged -> _state.value =
                _state.value.copy(email = event.value)
            is EditProfileEvent.UsernameChanged -> _state.value =
                _state.value.copy(username = event.value)
            is EditProfileEvent.ChangePasswordClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ChangePasswordClicked
                    )
                }
            }
            is EditProfileEvent.ChangePasswordConfirmed -> {
                userRepository.resetPassword()
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ChangePasswordConfirmed
                    )
                }
            }
            EditProfileEvent.ChangeProfileClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ChangeProfileClicked
                    )
                }
            }
        }
    }
}

data class EditProfileViewModelContainer(
    var state: SettingsStates,
    var onEvent: (SettingsEvent) -> Unit,
    var events: SharedFlow<SettingsEvent>,
)