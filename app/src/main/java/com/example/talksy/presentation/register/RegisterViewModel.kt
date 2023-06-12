package com.example.talksy.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(RegisterStates())
    val state: State<RegisterStates> = _state

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events = _events.asSharedFlow()

    private fun registerUser() {
        checkIfFieldsValid(
            _state.value.usernameInput,
            _state.value.emailInput,
            _state.value.passwordInput
        ) { errorMessage ->
            onEvent(RegisterEvent.ShowMessage(errorMessage))
            return@checkIfFieldsValid
        }

        viewModelScope.launch{
            userRepository.addNewUser(
                _state.value.usernameInput,
                _state.value.emailInput,
                _state.value.passwordInput
            ) { errorMessage ->
                onEvent(RegisterEvent.ShowMessage(errorMessage))
                return@addNewUser
            }
            if(userRepository.getUser() != null) onEvent(RegisterEvent.GoToApp)
        }
    }

    private fun checkIfFieldsValid(
        name: String = "  ", //spaces in the string are to pass name length checking.
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        if (name.length < 2) {
            errorMessage("Name must be at-least 2 characters.")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage("Valid Email must be entered.")
        } else if (password.length < 8) {
            errorMessage("Password Length must be at-least 8 characters.")
        }
    }

    fun onEvent(event: RegisterEvent){
        when(event){
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
                _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
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
}