package com.example.talksy.presentation.register

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.MainRepository
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(RegisterStates())
    val state: MutableState<RegisterStates> = _state

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events = _events.asSharedFlow()

    private fun registerUser() {
        showProgressDialog(true)
        viewModelScope.launch {
            mainRepository.addNewUser(username = _state.value.usernameInput,
                email = _state.value.emailInput,
                _state.value.passwordInput,
                errorMessage = { errorMessage ->
                    onEvent(RegisterEvent.ShowMessage(errorMessage))
                    showProgressDialog(false)
                    return@addNewUser
                },
                onFinish = {
                    if (mainRepository.isUserLoggedIn()) {
                        showProgressDialog(false)
                        onEvent(RegisterEvent.GoToApp)
                    }
                })
        }
    }

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameEntered -> {
                _state.value = _state.value.copy(usernameInput = event.value)
            }

            is RegisterEvent.EmailEntered -> {
                _state.value = _state.value.copy(emailInput = event.value)
            }

            is RegisterEvent.PasswordEntered -> {
                _state.value = _state.value.copy(passwordInput = event.value)
            }

            is RegisterEvent.GoToLoginClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        RegisterEvent.GoToLoginClicked
                    )
                }
            }

            is RegisterEvent.GoBackClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        RegisterEvent.GoBackClicked
                    )
                }
            }

            is RegisterEvent.PasswordVisibilityClicked -> {
                _state.value =
                    _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
            }

            is RegisterEvent.RegisterClicked -> {
                registerUser()
            }

            is RegisterEvent.ShowMessage -> {
                viewModelScope.launch {
                    _events.emit(
                        RegisterEvent.ShowMessage(event.message)
                    )
                }
            }

            is RegisterEvent.GoToApp -> {
                viewModelScope.launch {
                    _events.emit(
                        RegisterEvent.GoToApp
                    )
                }
            }
        }
    }

    private fun showProgressDialog(state: Boolean) {
        _state.value = _state.value.copy(showProgressDialog = state)
    }
}