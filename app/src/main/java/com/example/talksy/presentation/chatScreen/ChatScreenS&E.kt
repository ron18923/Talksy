package com.example.talksy.presentation.chatScreen

import android.net.Uri
import com.example.talksy.data.dataModels.Message

sealed class ChatScreenEvent() {
    data class InputChange(val input: String) : ChatScreenEvent()
    data class SetUser2(val user2: String) : ChatScreenEvent()

    object GoBackClicked : ChatScreenEvent()
}

data class ChatScreenStates(
    val otherProfile: Uri = Uri.EMPTY,
    val user2: String = "",
    val inputText: String = "",
    val messages: ArrayList<MessageChatScreen> = arrayListOf()
)

data class MessageChatScreen(
    val message: String,
    val isItMe: Boolean,
)
