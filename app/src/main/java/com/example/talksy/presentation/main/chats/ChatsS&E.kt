package com.example.talksy.presentation.main.chats

sealed class ChatsEvent {
    data class ChatClicked(val username: String): ChatsEvent()
}

data class ChatsState(
    var chats: ArrayList<HashMap<String, String>> = arrayListOf()
)