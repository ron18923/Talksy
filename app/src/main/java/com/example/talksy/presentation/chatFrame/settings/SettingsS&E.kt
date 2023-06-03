package com.example.talksy.presentation.chatFrame.settings

sealed class SettingsEvent {
    object SignOut : SettingsEvent()
}

data class SettingsStates(
    var displayName: String = "",
    var email: String = "",
)