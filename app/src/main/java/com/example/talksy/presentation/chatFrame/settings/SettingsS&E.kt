package com.example.talksy.presentation.chatFrame.settings

import android.net.Uri

sealed class SettingsEvent {
    object SignOut : SettingsEvent()
    object GoToEditProfile :SettingsEvent()
}

data class SettingsStates(
    var username: String = "",
    var email: String = "",
    var profilePicture: Uri = Uri.EMPTY
)