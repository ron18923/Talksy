     package com.example.talksy.data.dataModels

import android.net.Uri
import java.util.UUID

     data class Chat(
    val messages: ArrayList<Message> = arrayListOf(),
    val uid1: String = "",
    val uid2: String = ""
)

data class ChatsListItem(
    val profilePicture: Uri = Uri.EMPTY,
    val username: String = "",
    val lastMessage: Message = Message()
)

data class ChatsListItemView(
    val profilePicture: Uri = Uri.EMPTY,
    val username: String = "",
    val lastMessage: MessageView = MessageView()
)

data class Message(
    val message: String = "",
    val timestamp: Long = -1,
    val senderUid: String = "",
    val id: String = UUID.randomUUID().toString(),
    val image: String = ""
)

data class MessageView(
    val message: String = "",
    val timestamp: String = "",
    val isItMe: Boolean = true,
    val image: Uri = Uri.EMPTY
)