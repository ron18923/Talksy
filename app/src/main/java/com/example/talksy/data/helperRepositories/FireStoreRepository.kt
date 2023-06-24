package com.example.talksy.data.helperRepositories

import com.example.talksy.data.dataModels.Chat
import com.example.talksy.data.dataModels.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FireStoreRepository {

    companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_CHATS = "chats"

        //user fields
        const val FIELD_CONTACTS = "contacts"
        const val FIELD_USERNAME = "username"
        const val FIELD_EMAIL = "email"
        const val FIELD_PROFILE_PICTURE = "profilePicture"
        const val FIELD_UID = "uid"

        //chat fields
        const val FIELD_MESSAGES = "messages"
        const val FIELD_MESSAGE = "message"
        const val FIELD_SENDER_UID = "senderUid"
        const val FIELD_UID1 = "uid1"
        const val FIELD_UID2 = "uid2"
    }

    private val usersCollection =
        FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
    private val chatsCollection =
        FirebaseFirestore.getInstance().collection(COLLECTION_CHATS)

    suspend fun addContactToUser(userUID: String, contactUsername: String) {

        val contactUid = getUserUidByUsername(contactUsername) ?: return

        val docRef = usersCollection.document(userUID)



        docRef.get().addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)

            user?.let {
                val contactsArray = user.contacts
                if (contactsArray.contains(contactUid)) return@addOnSuccessListener
                contactsArray.add(contactUid)
                docRef.set(hashMapOf(FIELD_CONTACTS to contactsArray))
            }
        }
    }

    fun addNewUser(user: User, uid: String) {
        val docRef = usersCollection.document(uid)
        docRef.set(
            hashMapOf(
                FIELD_CONTACTS to user.contacts,
                FIELD_EMAIL to user.email,
                FIELD_PROFILE_PICTURE to user.profilePicture,
                FIELD_USERNAME to user.username
            )
        )
    }

    suspend fun searchUsers(searchValue: String): ArrayList<String> {
        // TODO: implement better search.
        var usernameList: ArrayList<String> = arrayListOf()
        usersCollection.get().await().documents.forEach { userDocument ->
            val user = userDocument.toObject(User::class.java) ?: return@forEach
            if (user.username.contains(searchValue)) usernameList.add(user.username)
        }
        usernameList = ArrayList(usernameList.take(10))
        return usernameList
    }

    private suspend fun getUserUidByUsername(username: String): String? {
        // TODO: if there are two users with the same username(shouldn't happen) then the first one will be added.
        val result = usersCollection.whereEqualTo(FIELD_USERNAME, username).get().await()
        if (result.isEmpty) return null
        return result.documents[0].id
    }

    suspend fun getUsernameByUid(uid: String): String? {
        val user = usersCollection.document(uid).get().await().toObject(User::class.java)
        return user?.username
    }

    suspend fun getUser(userUid: String): User? {
        val docRef = usersCollection.document(userUid)

        val user = docRef.get().await().toObject(User::class.java)
        return user
    }

    suspend fun getProfilePicture(uid: String): String {

        val docRef = usersCollection.document(uid)

        val user = docRef.get().await().toObject(User::class.java) ?: return ""

        return user.profilePicture
    }

    suspend fun checkUsername(username: String, errorMessage: (String) -> Unit): Boolean {
        val documents = usersCollection.whereEqualTo(FIELD_USERNAME, username).get().await()
        return if (documents.isEmpty) {
            true
        } else {
            errorMessage("Username is already being used.")
            false
        }
    }

    suspend fun getChat(user1: String, user2: String): Chat? {
        val firstQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, user1).whereEqualTo(FIELD_UID2, user2).get()
                .await()
        val secondQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, user2).whereEqualTo(FIELD_UID2, user1).get()
                .await()

        lateinit var finalQuery: QuerySnapshot

        if (firstQuery.isEmpty && secondQuery.isEmpty) {
            createChat(user1, user2)

            finalQuery =
                chatsCollection.whereEqualTo(FIELD_UID1, user1).whereEqualTo(FIELD_UID2, user2)
                    .get()
                    .await()
        } else if (!firstQuery.isEmpty) {
            finalQuery = firstQuery
        } else if (!secondQuery.isEmpty) {
            finalQuery = secondQuery
        }
        return finalQuery.documents[0].toObject(Chat::class.java)
    }

    private suspend fun createChat(user1: String, user2: String) {
        val docRef = chatsCollection.document()
        docRef.set(
            hashMapOf(
                FIELD_MESSAGES to arrayListOf<HashMap<String, String>>(),
                FIELD_UID1 to user1,
                FIELD_UID2 to user2,
            )
        ).await()
    }

    suspend fun updateChat(chat: Chat) {
        val firstQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, chat.uid1).whereEqualTo(FIELD_UID2, chat.uid2)
                .get()
                .await()
        val secondQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, chat.uid2).whereEqualTo(FIELD_UID2, chat.uid1)
                .get()
                .await()

        lateinit var finalQuery: QuerySnapshot

        if (!firstQuery.isEmpty) finalQuery = firstQuery
        else if (!secondQuery.isEmpty) finalQuery = secondQuery

        chatsCollection.document(finalQuery.documents[0].id).set(chat)
    }
}