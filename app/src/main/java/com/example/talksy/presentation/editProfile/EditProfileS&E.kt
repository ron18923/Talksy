package com.example.talksy.presentation.editProfile

sealed class EditProfileEvent {
    data class EmailChanged(val value: String) : EditProfileEvent()
    data class UsernameChanged(val value: String) : EditProfileEvent()

    object GoBackClicked : EditProfileEvent()
    object ChangePasswordClicked: EditProfileEvent()
    object ChangePasswordConfirmed: EditProfileEvent()
    object ChangeProfileClicked: EditProfileEvent()
}

data class EditProfileStates(
    val email: String = "",
    val username: String = ""
)