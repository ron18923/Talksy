package com.example.talksy.data.helperRepositories

import com.example.talksy.data.dataModels.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreRepository {

    companion object {
        const val COLLECTION_USERS = "users"

        const val FIELD_CONTACTS = "contacts"
        const val FIELD_USERNAME = "username"
        const val FIELD_UID = "uid"
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)

    suspend fun addContactToUser(userUID: String, contactUsername: String) {

        val contactUid = getUserUidByUsername(contactUsername) ?: return

        val docRef = usersCollection.document(userUID)



        docRef.get().addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)

            user?.let {
                val contactsArray = user.contacts
                if(contactsArray.contains(contactUid)) return@addOnSuccessListener
                contactsArray.add(contactUid)
                docRef.set(hashMapOf(FIELD_CONTACTS to contactsArray) )
            }
        }
    }

    fun addNewUser(user: User, uid: String) {
        // TODO: implement fully
        val docRef = usersCollection.document(uid)
    }

    suspend fun searchUsers(searchValue: String): ArrayList<String> {
        // TODO: implement better search.
        var usernameList: ArrayList<String> = arrayListOf()
        usersCollection.get().await().documents.forEach { userDocument ->
            val user = userDocument.toObject(User::class.java) ?: return@forEach
            if(user.username.contains(searchValue)) usernameList.add(user.username)
        }
        usernameList = ArrayList(usernameList.take(10))
        return usernameList
    }

    private suspend fun getUserUidByUsername(username: String): String? {
        // TODO: if there are two users with the same username(shouldn't happen) then the first one will be added.
        val result = usersCollection.whereEqualTo(FIELD_USERNAME, username).get().await()
        if(result.isEmpty) return null
        return result.documents[0].id
    }

    private suspend fun getUserUsernameByUid(uid: String): String?{
        val user = usersCollection.document(uid).get().await().toObject(User::class.java)
        return user?.username
    }
}