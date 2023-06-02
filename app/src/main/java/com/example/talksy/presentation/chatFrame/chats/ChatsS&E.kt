package com.example.talksy.presentation.chatFrame.chats

sealed class ChatsEvent {

}

data class ChatsState(
    var temp: String = ""
)