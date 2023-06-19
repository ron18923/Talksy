package com.example.talksy.presentation.chatScreen

sealed class ChatScreenEvent(){
    object GoBackClicked : ChatScreenEvent()
}

data class ChatScreenStates(
    val string: String = ""
)
