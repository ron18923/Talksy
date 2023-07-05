package com.example.talksy.presentation.main.contacts

sealed class ContactsEvent {
    data class SearchEntered(val value: String) : ContactsEvent()
    data class NewContactClicked(val username: String) : ContactsEvent()
    data class ExistingContactClicked(val username: String?) : ContactsEvent()

    object SearchClose : ContactsEvent()
    object Dispose : ContactsEvent()
}

data class ContactsStates(
    var searchInput: String = "",
    var searchList: ArrayList<String> = arrayListOf(),
    var contactsList: ArrayList<HashMap<String, String>> = arrayListOf(),
    var showProgressBar: Boolean = true
)