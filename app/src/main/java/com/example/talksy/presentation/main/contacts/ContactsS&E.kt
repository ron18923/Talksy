package com.example.talksy.presentation.main.contacts

import com.example.talksy.data.dataModels.Contact

sealed class ContactsEvent {
    data class SearchEntered(val value: String): ContactsEvent()
    data class NewContactClicked(val username: String): ContactsEvent()
    data class ExistingContactClicked(val username: String?): ContactsEvent()

    object SearchClose: ContactsEvent()
}

data class ContactsStates(
    var searchInput: String = "",
    var searchList: ArrayList<String> = arrayListOf(),
    var contactsList: ArrayList<HashMap<String, String>> = arrayListOf()
)