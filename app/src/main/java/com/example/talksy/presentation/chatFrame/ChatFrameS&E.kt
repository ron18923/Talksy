package com.example.talksy.presentation.chatFrame

sealed class ChatFrameEvent {
    object NoUserFound : ChatFrameEvent()

    data class NavItemSelected(val value: Int) : ChatFrameEvent()
}

data class ChatFrameStates(
    var selectedNavItem: Int = 0
)