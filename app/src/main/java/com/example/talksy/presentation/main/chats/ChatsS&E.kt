package com.example.talksy.presentation.main.chats

sealed class ChatsEvent {

}

data class ChatsState(
    var temp: String = ""
)