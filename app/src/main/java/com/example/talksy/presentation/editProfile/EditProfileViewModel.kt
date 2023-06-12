package com.example.talksy.presentation.editProfile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.StorageRepository
import com.example.talksy.data.UserRepository
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) :
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

            is EditProfileEvent.ImagePicked -> {
                viewModelScope.launch {
                    val userUid = userRepository.getUserUid() ?: return@launch
                    storageRepository.putProfilePicture(userUid, event.value) {
                        viewModelScope.launch {
                            userRepository.updateProfilePicture(it)
                            _state.value =
                                _state.value.copy(profileImage = it)
                            Log.d(TAG, "onEvent: $it")
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