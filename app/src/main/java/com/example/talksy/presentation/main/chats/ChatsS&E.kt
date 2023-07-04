package com.example.talksy.presentation.main.chats

import com.example.talksy.data.dataModels.ChatsListItem
import com.example.talksy.data.dataModels.ChatsListItemView

sealed class ChatsEvent {
    data class ChatClicked(val username: String): ChatsEvent()

    object Dispose : ChatsEvent()
}

data class ChatsState(
    var chats: ArrayList<ChatsListItemView> = arrayListOf()
)