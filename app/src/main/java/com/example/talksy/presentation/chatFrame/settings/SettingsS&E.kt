package com.example.talksy.presentation.chatFrame.settings

sealed class SettingsEvent {
    object SignOut : SettingsEvent()
    object GoToEditProfile :SettingsEvent()
}

data class SettingsStates(
    var username: String = "",
    var email: String = "",
)