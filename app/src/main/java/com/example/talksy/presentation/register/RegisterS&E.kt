package com.example.talksy.presentation.register

sealed class RegisterEvent {
    data class UsernameEntered(val value: String) : RegisterEvent()
    data class EmailEntered(val value: String) : RegisterEvent()
    data class PasswordEntered(val value: String) : RegisterEvent()

    object PasswordVisibilityClicked : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object GoToLoginClicked : RegisterEvent()
    object GoBackClicked : RegisterEvent()

    data class ShowMessage(val message: String) : RegisterEvent()
    object GoToApp : RegisterEvent()
}

data class RegisterStates(
    var usernameInput: String = "",
    var emailInput: String = "",
    var passwordInput: String = "",
    var isPasswordVisible: Boolean = false
)