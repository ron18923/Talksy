package com.example.talksy.presentation.chatFrame.contacts

sealed class ContactsEvent {
    data class SearchEntered(val value: String): ContactsEvent()
    data class AddNewContact(val username: String): ContactsEvent()

    object SearchClose: ContactsEvent()
}

data class ContactsStates(
    var searchInput: String = "",
    var searchList: ArrayList<String> = arrayListOf(),
    var contactsList: ArrayList<HashMap<String, String>> = arrayListOf()
)