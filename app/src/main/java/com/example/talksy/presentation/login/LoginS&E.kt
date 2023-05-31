package com.example.talksy.presentation.login

sealed class LoginEvent {
    data class EmailEntered(val value: String) : LoginEvent()
    data class PasswordEntered(val value: String) : LoginEvent()

    object PasswordVisibilityClicked : LoginEvent()
    object LoginClicked : LoginEvent()
    object GoToRegisterClicked : LoginEvent()
    object GoBackClicked : LoginEvent()

    data class ShowMessage(val message: String) : LoginEvent()
    object GoToApp : LoginEvent()
}

data class LoginStates(
    var emailInput: String = "",
    var passwordInput: String = "",
    var isPasswordVisible: Boolean = false
)