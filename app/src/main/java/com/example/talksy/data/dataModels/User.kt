package com.example.talksy.data.dataModels

data class User(
    val username: String = "",
    val email: String = "",
    var profilePicture: String = "",
    var contacts: ArrayList<String> = ArrayList()
)
