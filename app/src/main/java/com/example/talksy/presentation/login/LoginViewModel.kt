package com.example.talksy.presentation.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoginStates())
    val state: MutableState<LoginStates> = _state

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    private fun loginUser() {
//        checkIfFieldsValid(
//            _state.value.emailInput,
//            _state.value.passwordInput
//        ) { errorMessage ->
//            onEvent(LoginEvent.ShowMessage(errorMessage))
//            return@checkIfFieldsValid
//        }
        showProgressDialog(true)
        viewModelScope.launch {
            mainRepository.signInUser(
                _state.value.emailInput,
                _state.value.passwordInput
            ) { errorMessage ->
                onEvent(LoginEvent.ShowMessage(errorMessage))
                showProgressDialog(false)
                return@signInUser
            }
            if (mainRepository.isUserLoggedIn()){
                showProgressDialog(false)
                onEvent(LoginEvent.GoToApp)
            }
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

    fun onEvent(event: LoginEvent) {
        when (event) {
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
                _state.value =
                    _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
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

            LoginEvent.ForgotPasswordClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        LoginEvent.ForgotPasswordClicked
                    )
                }
            }

            LoginEvent.ForgotPasswordDialogClicked -> mainRepository.resetPassword(_state.value.emailInput)
        }
    }

    private fun showProgressDialog(state: Boolean) {
        _state.value = _state.value.copy(showProgressDialog = state)
    }
}