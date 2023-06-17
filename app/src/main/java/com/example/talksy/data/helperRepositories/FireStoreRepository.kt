package com.example.talksy.data.helperRepositories

import com.example.talksy.data.dataModels.User
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreRepository {

    companion object {
        const val USERS = "users"
        const val CONTACTS = "contacts"
    }

    fun addContactToUser(userUID: String, contactUID: String) {
        val docRef = FirebaseFirestore.getInstance().collection(USERS).document(userUID)
        docRef.get().addOnSuccessListener { document ->
            val currentArray = document.get(CONTACTS) as? ArrayList<String> ?: ArrayList()
            currentArray.add(contactUID)
            val updatedData = hashMapOf(CONTACTS to currentArray)
            docRef.set(updatedData)
        }
    }

    fun addNewUser(user: User){
        val docRef = FirebaseFirestore.getInstance().collection(USERS).document(user.uid)
    }
}