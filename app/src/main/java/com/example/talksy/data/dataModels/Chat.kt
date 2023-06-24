package com.example.talksy.data.dataModels

data class Chat(
    val messages: ArrayList<Message> = arrayListOf(),
    val uid1: String = "",
    val uid2: String = ""
)

data class Message(
    val message: String = "",
    val senderUid: String = ""
)
