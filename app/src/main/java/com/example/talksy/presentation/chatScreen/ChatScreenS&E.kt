package com.example.talksy.presentation.chatScreen

import android.net.Uri

sealed class ChatScreenEvent() {
    object GoBackClicked : ChatScreenEvent()
}

data class ChatScreenStates(
    val otherUsername: String = "",
    val otherProfile: Uri = Uri.EMPTY
)
