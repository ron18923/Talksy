package com.example.talksy.data.helperRepositories

import android.util.Log
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.dataModels.Chat
import com.example.talksy.data.dataModels.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await

class FireStoreRepository {
    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_CHATS = "chats"

        //user fields
        private const val FIELD_CONTACTS = "contacts"
        private const val FIELD_USERNAME = "username"
        private const val FIELD_EMAIL = "email"
        private const val FIELD_PROFILE_PICTURE = "profilePicture"
        private const val FIELD_UID = "uid"

        //chat fields
        private const val FIELD_MESSAGES = "messages"
        private const val FIELD_MESSAGE = "message"
        private const val FIELD_SENDER_UID = "senderUid"
        private const val FIELD_UID1 = "uid1"
        private const val FIELD_UID2 = "uid2"
    }

    private val fireStore = FirebaseFirestore.getInstance()
    private val usersCollection =
        fireStore.collection(COLLECTION_USERS)
    private val chatsCollection =
        fireStore.collection(COLLECTION_CHATS)


    suspend fun addContactToUser(userUID: String, contactUsername: String) {

        val contactUid = getUserUidByUsername(contactUsername) ?: return

        val docRef = usersCollection.document(userUID)



        docRef.get().addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)

            user?.let {
                val contactsArray = user.contacts
                if (contactsArray.contains(contactUid)) return@addOnSuccessListener
                contactsArray.add(contactUid)
                docRef.set(hashMapOf(FIELD_CONTACTS to contactsArray), SetOptions.merge())
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

    suspend fun getUserUidByUsername(username: String): String? {
        // TODO: if there are two users with the same username(shouldn't happen) then the first one will be added.
        val result = usersCollection.whereEqualTo(FIELD_USERNAME, username).get().await()
        if (result.isEmpty) return null
        return result.documents[0].id
    }

    suspend fun getUsernameByUid(uid: String): String? {
        val user = usersCollection.document(uid).get().await().toObject(User::class.java)
        return user?.username
    }

    private suspend fun getUserFlow(userUid: String) = callbackFlow {
        val snapshotListener =
            usersCollection.document(userUid).addSnapshotListener { value, error ->
                val response = if (error == null) {
                    UserResponse.OnSuccess(value)
                } else {
                    UserResponse.OnError(error)
                }
                Log.d(TAG, "getUserFlow: sent")
                trySend(response)
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getUser(userUid: String, user: (User?) -> Unit) {
        getUserFlow(userUid).collectLatest {
            when (it) {
                is UserResponse.OnError -> TODO()
                is UserResponse.OnSuccess -> {
                    Log.d(TAG, "getUserFlow: sent to 2")
                    val u =
                        it.documentSnapshot?.reference?.get()?.await()?.toObject(User::class.java)
                    user(u)
                    Log.d(TAG, "getUser: ${u}")
                }
            }
        }
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

    private suspend fun getChatFlowSetup(userUid1: String, userUid2: String) = callbackFlow {
        val firstQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, userUid1).whereEqualTo(FIELD_UID2, userUid2)
                .get()
                .await()
        val secondQuery =
            chatsCollection.whereEqualTo(FIELD_UID1, userUid2).whereEqualTo(FIELD_UID2, userUid1)
                .get()
                .await()

        lateinit var finalQuery: Query

        if (firstQuery.isEmpty && secondQuery.isEmpty) {
            createChat(userUid1, userUid2)

            finalQuery =
                chatsCollection.whereEqualTo(FIELD_UID1, userUid1)
                    .whereEqualTo(FIELD_UID2, userUid2)

        } else if (!firstQuery.isEmpty) {
            finalQuery = chatsCollection.whereEqualTo(FIELD_UID1, userUid1)
                .whereEqualTo(FIELD_UID2, userUid2)
        } else if (!secondQuery.isEmpty) {
            finalQuery = chatsCollection.whereEqualTo(FIELD_UID1, userUid2)
                .whereEqualTo(FIELD_UID2, userUid1)
        }
        Log.d(TAG, "getChatFlow: 333333333333333")
        val snapshotListener = finalQuery.addSnapshotListener { value, error ->
            Log.d(TAG, "getChatFlow: 444444444444444")
            val response = if (error == null) {
                ChatResponse.OnSuccess(value)
            } else {
                ChatResponse.OnError(error)
            }
            Log.d(TAG, "getChatFlow: sent")
            trySend(response)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getChatFlow(userUid1: String, userUid2: String, chat: (Chat?) -> Unit) {
        getChatFlowSetup(userUid1, userUid2).collectLatest {
            when (it) {
                is ChatResponse.OnError -> TODO()
                is ChatResponse.OnSuccess -> {
                    Log.d(TAG, "getChatFlow: sent to getChat")
                    chat(it.querySnapshot?.documents?.get(0)?.toObject(Chat::class.java))
                }
            }
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
        Log.d(TAG, "updateChat: message should be added.")
    }

    private suspend fun getUserChatsFlowSetup(userUid: String) = callbackFlow {
        val query = chatsCollection.where(
            Filter.or(
                Filter.equalTo(FIELD_UID1, userUid),
                Filter.equalTo(FIELD_UID2, userUid)
            )
        )

        val snapshotListener = query.addSnapshotListener { value, error ->
            val response = if (error == null) {
                ChatResponse.OnSuccess(value)
            } else {
                ChatResponse.OnError(error)
            }
            trySend(response)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getUserChatsFlow(userUid: String, chat: (ArrayList<Chat>) -> Unit) {
        getUserChatsFlowSetup(userUid).collectLatest {
            val chatsList: ArrayList<Chat> = arrayListOf()
            when (it) {
                is ChatResponse.OnError -> TODO()
                is ChatResponse.OnSuccess -> {
                    it.querySnapshot?.documents?.forEach { document ->
                        chatsList.add(document.toObject(Chat::class.java)!!)
                    }
                    chat(chatsList)
                }
            }
        }
    }
}

sealed class ChatResponse {
    data class OnSuccess(val querySnapshot: QuerySnapshot?) : ChatResponse()
    data class OnError(val exception: FirebaseFirestoreException?) : ChatResponse()
}

sealed class UserResponse {
    data class OnSuccess(val documentSnapshot: DocumentSnapshot?) : UserResponse()
    data class OnError(val exception: FirebaseFirestoreException?) : UserResponse()
}