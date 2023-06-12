package com.example.talksy.presentation.editProfile

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class EditProfileEvent {
    data class EmailChanged(val value: String) : EditProfileEvent()
    data class UsernameChanged(val value: String) : EditProfileEvent()
    data class ImagePicked(val value: Uri) : EditProfileEvent()

    object GoBackClicked : EditProfileEvent()
    object ChangePasswordClicked: EditProfileEvent()
    object ChangePasswordConfirmed: EditProfileEvent()
    object ChangeProfileClicked: EditProfileEvent()
}

data class EditProfileStates(
    var email: String = "",
    var username: String = "",
    var profileImage: Uri = Uri.EMPTY
)