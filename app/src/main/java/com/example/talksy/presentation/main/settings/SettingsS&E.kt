package com.example.talksy.presentation.main.settings

import android.net.Uri
import coil.Coil

sealed class SettingsEvent {
    object SignOut : SettingsEvent()
    object GoToEditProfile : SettingsEvent()
    object Dispose : SettingsEvent()
}

data class SettingsStates(
    var username: String = "",
    var email: String = "",
    var profilePicture: Uri = Uri.EMPTY,
)