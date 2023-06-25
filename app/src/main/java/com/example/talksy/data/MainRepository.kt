package com.example.talksy.data

import android.net.Uri
import com.example.talksy.data.dataModels.Chat
import com.example.talksy.data.dataModels.Message
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRepository(
    val userRepository: UserRepository,
    val storageRepository: StorageRepository,
    val fireStoreRepository: FireStoreRepository
) {

    suspend fun updateProfilePicture(profilePicture: Uri) =
        userRepository.updateProfilePicture(profilePicture)

    suspend fun signInUser(email: String, password: String, errorMessage: (String) -> Unit) =
        userRepository.signInUser(email, password, errorMessage)

    fun signOutUser() = userRepository.signOutUser()

    fun getUser(): FirebaseUser? = userRepository.getUser()

    fun resetPassword() = userRepository.resetPassword()

    fun getUserUid(): String? = userRepository.getUserUid()

    suspend fun addNewUser(
        username: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        if (!fireStoreRepository.checkUsername(username, errorMessage)) return
        val userUid =
            userRepository.addNewUser(username = username, email = email, password, errorMessage)
        userUid?.let {
            val user = User(username = username, email = email)
            fireStoreRepository.addNewUser(user = user, uid = userUid)
        }
    }

    suspend fun putProfilePicture(
        uid: String,
        profilePicture: Uri,
        profilePictureUri: (Uri) -> Unit
    ) = storageRepository.putProfilePicture(
        uid = uid,
        profilePicture = profilePicture
    ) { resultUri ->
        profilePictureUri(resultUri)
    }

    suspend fun deleteProfilePicture(uid: String, profilePictureDeleted: () -> Unit) =
        storageRepository.deleteProfilePicture(
            uid = uid,
            profilePictureDeleted = profilePictureDeleted
        )

    suspend fun searchUsers(searchUsers: String): ArrayList<String> =
        fireStoreRepository.searchUsers(searchUsers)

    suspend fun addNewContact(username: String) {
        val currentUser = userRepository.getUserUid()
        currentUser?.let {
            fireStoreRepository.addContactToUser(userUID = currentUser, contactUsername = username)
        }
    }

    suspend fun getUserContacts(): ArrayList<HashMap<String, String>> {
        val user = userRepository.getUser() ?: return arrayListOf()

        val contacts: ArrayList<HashMap<String, String>> = arrayListOf()
        val currentUserUid = user.uid
        val userFireStore =
            fireStoreRepository.getUser(userUid = currentUserUid) ?: return arrayListOf()
        val currentUserContacts = userFireStore.contacts
        currentUserContacts.forEach { uid ->
            val profilePicture = storageRepository.getProfilePicture(uid)
            val username = fireStoreRepository.getUsernameByUid(uid) ?: return@forEach
            contacts.add(
                hashMapOf(
                    "username" to username,
                    "profilePicture" to profilePicture.toString()
                )
            )
        }
        return contacts
    }

    fun setUserListener(listener: UserStateListener) = userRepository.setListener(listener)

    suspend fun getChat(userName2: String, chat: (Chat?) -> Unit) {
        val userUid1 = userRepository.getUserUid() ?: return
        val userUid2 = fireStoreRepository.getUserUidByUsername(userName2) ?: return

        fireStoreRepository.getChat(userUid1 = userUid1, userUid2 = userUid2) { chatResult ->
            chat(chatResult)
        }
    }

    fun addMessage(message: String, chat: Chat) {
        val senderUid = userRepository.getUserUid()
        senderUid?.let {
            chat.messages.add(Message(message, senderUid))
        }
    }

    suspend fun addMessage(message: String, user2: String) {

        val senderUid = userRepository.getUserUid() ?: return

        getChat(user2) { chat ->
            if (chat == null) return@getChat

            chat.messages.add(Message(message, senderUid))
            CoroutineScope(Dispatchers.IO).launch {
                fireStoreRepository.updateChat(chat)
            }

        }
    }

    fun isMessageFromMe(message: Message): Boolean {
        val senderUid = message.senderUid
        val userUid = userRepository.getUserUid()
        return senderUid == userUid
    }
}