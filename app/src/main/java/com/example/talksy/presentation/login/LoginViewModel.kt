package com.example.talksy.presentation.login

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoginStates())
    val state: State<LoginStates> = _state

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    private fun loginUser() {
        checkIfFieldsValid(
            _state.value.emailInput,
            _state.value.passwordInput
        ) { errorMessage ->
            onEvent(LoginEvent.ShowMessage(errorMessage))
            return@checkIfFieldsValid
        }

        viewModelScope.launch{
            userRepository.signInUser(
                _state.value.emailInput,
                _state.value.passwordInput
            ) { errorMessage ->
                onEvent(LoginEvent.ShowMessage(errorMessage))
                return@signInUser
            }
            if(userRepository.getUser() != null) onEvent(LoginEvent.GoToApp)
        }
    }

    private fun checkIfFieldsValid(
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage("Valid Email must be entered.")
        } else if (password.length < 8) {
            errorMessage("Password Length must be at-least 8 characters.")
        }
    }

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EmailEntered -> {
                _state.value = _state.value.copy(emailInput = event.value)
            }
            is LoginEvent.PasswordEntered -> {
                _state.value = _state.value.copy(passwordInput = event.value)
            }
            is LoginEvent.GoToRegisterClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        LoginEvent.GoToRegisterClicked
                    )
                }
            }
            is LoginEvent.GoBackClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        LoginEvent.GoBackClicked
                    )
                }
            }
            is LoginEvent.PasswordVisibilityClicked -> {
                _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
            }
            is LoginEvent.LoginClicked -> {
                loginUser()
            }
            is LoginEvent.ShowMessage -> {
                viewModelScope.launch {
                    _events.emit(
                        LoginEvent.ShowMessage(event.message)
                    )
                }
            }
            is LoginEvent.GoToApp -> {
                viewModelScope.launch {
                    _events.emit(
                        LoginEvent.GoToApp
                    )
                }
            }
        }
    }
}