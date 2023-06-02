package com.example.talksy.presentation.chatFrame.contacts

sealed class ContactsEvent {

}

data class ContactsStates(
    var temp: String = ""
)