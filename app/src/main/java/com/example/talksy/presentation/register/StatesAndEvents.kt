package com.example.talksy.presentation.register

sealed class RegisterEvent {
    data class NameEntered(val value: String) : RegisterEvent()
    data class EmailEntered(val value: String) : RegisterEvent()
    data class PasswordEntered(val value: String) : RegisterEvent()

    object PasswordVisibilityClicked : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object GoToLoginClicked : RegisterEvent()

    data class ShowMessage(val message: String) : RegisterEvent()
}

data class RegisterStates(
    var nameInput: String = "",
    var emailInput: String = "",
    var passwordInput: String = "",
    var isPasswordVisible: Boolean = false,
    var isUserLogged: Boolean = false
)