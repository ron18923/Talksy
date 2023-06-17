package com.example.talksy.presentation.chatFrame.contacts

import androidx.compose.ui.text.input.TextFieldValue

sealed class ContactsEvent {
    data class SearchEntered(val value: TextFieldValue): ContactsEvent()
    object SearchClose: ContactsEvent()
}

data class ContactsStates(
    var searchInput: TextFieldValue = TextFieldValue("")
)