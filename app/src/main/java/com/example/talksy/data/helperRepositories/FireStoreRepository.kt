package com.example.talksy.data.helperRepositories

import com.google.firebase.firestore.FirebaseFirestore

class FireStoreRepository {

    companion object {
        const val USERS = "users"
        const val CONTACTS = "contacts"
    }

    fun addContactToUser(userUID: String, contactUID: String) {
        val docRef = FirebaseFirestore.getInstance().collection(USERS).document(userUID)
        docRef.get().addOnSuccessListener { document ->
            val currentArray = document.get(CONTACTS) as? ArrayList<Contact> ?: ArrayList()
            currentArray.add(Contact("asdasd", "Ron"))
            val updatedData = hashMapOf(CONTACTS to currentArray)
            docRef.set(updatedData)
        }
    }

    data class Contact(
        var uid: String,
        var username: String,
    )
//
//    data class Message(
//
//    )
}