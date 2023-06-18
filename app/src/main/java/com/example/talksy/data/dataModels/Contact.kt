package com.example.talksy.data.dataModels

data class Contact(
    val uid: String,
    val username: String
)

data class ContactInfo(
    var contactList: ArrayList<Contact> = arrayListOf()
)