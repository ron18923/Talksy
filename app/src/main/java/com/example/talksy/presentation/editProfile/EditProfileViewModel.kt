package com.example.talksy.presentation.editProfile

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository
) :
    ViewModel() {

    private var _state = mutableStateOf(EditProfileStates())
    val state: MutableState<EditProfileStates> = _state

    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events = _events.asSharedFlow()

    private val _user = mainRepository.getUser()

    init {
        updateScreenValues()
    }

    private fun updateScreenValues() {
        _user?.let {
            _state.value = _state.value.copy(
                email = _user.email ?: "",
                username = _user.displayName ?: "",
                profileImage = _user.photoUrl ?: Uri.EMPTY
            )
        }
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

            EditProfileEvent.ChangePasswordClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ChangePasswordClicked
                    )
                }
            }

            EditProfileEvent.ChangePasswordConfirmed -> {
                mainRepository.resetPassword()
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ChangePasswordConfirmed
                    )
                }
            }

            EditProfileEvent.ProfileImageClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        EditProfileEvent.ProfileImageClicked
                    )
                }
            }

            is EditProfileEvent.ImagePicked -> {
                viewModelScope.launch {
                    val userUid = mainRepository.getUserUid() ?: return@launch
                    mainRepository.putProfilePicture(userUid, event.value) {
                        viewModelScope.launch {
                            mainRepository.updateProfilePicture(it)
                            _state.value =
                                _state.value.copy(profileImage = it)
                        }
                    }
                }
            }

            EditProfileEvent.DeleteImageClicked -> {
                viewModelScope.launch {
                    val userUid = mainRepository.getUserUid() ?: return@launch
                    mainRepository.deleteProfilePicture(userUid) {
                        viewModelScope.launch {
                            mainRepository.updateProfilePicture(Uri.EMPTY)
                            _state.value =
                                _state.value.copy(profileImage = Uri.EMPTY)
                        }
                    }
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