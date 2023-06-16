package com.example.talksy.data.dataModels

import android.net.Uri

data class User(
    val username: String,
    val email: String,
    var profilePicture: Uri = Uri.EMPTY,
    var contacts: ArrayList<String> = ArrayList()
)
