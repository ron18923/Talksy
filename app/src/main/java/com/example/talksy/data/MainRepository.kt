package com.example.talksy.data

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.dataModels.Chat
import com.example.talksy.data.dataModels.ChatsListItem
import com.example.talksy.data.dataModels.Message
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(
    val userRepository: UserRepository,
    val storageRepository: StorageRepository,
    val fireStoreRepository: FireStoreRepository
) {

    suspend fun signInUser(email: String, password: String, errorMessage: (String) -> Unit) =
        userRepository.signInUser(email, password, errorMessage)

    fun signOutUser() = userRepository.signOutUser()

    suspend fun getUser(user: (User?) -> Unit) {
        getUserUid()?.let { uid ->
            fireStoreRepository.getUser(uid) { user ->
                user(user)
            }
        }
    }

    fun isUserLoggedIn(): Boolean = userRepository.getUser() != null

    fun resetPassword() = userRepository.resetPassword()

    fun getUserUid(): String? = userRepository.getUserUid()

    suspend fun addNewUser(
        username: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit,
        onFinish: () -> Unit
    ) {
        if (!fireStoreRepository.checkUsername(username, errorMessage)) return
        val userUid =
            userRepository.addNewUser(username = username, email = email, password, errorMessage)
        userUid?.let {
            val user = User(username = username, email = email)
            fireStoreRepository.addNewUser(user = user, uid = userUid)
        }
        onFinish()
    }

    suspend fun putProfilePicture(
        uid: String,
        profilePicture: Uri,
        pictureAdded: (Uri) -> Unit
    ){
        val profilePictureUri = storageRepository.putProfilePicture(
            uid = uid,
            profilePicture = profilePicture
        )
        userRepository.updateProfilePicture(profilePictureUri)
        fireStoreRepository.setProfilePicture(userUid = uid, imageUri = profilePictureUri) {
            pictureAdded(profilePictureUri)
        }
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

    suspend fun getUserContacts(contactsResult: (ArrayList<HashMap<String, String>>) -> Unit) {

        val user = userRepository.getUser() ?: return

        fireStoreRepository.getUser(user.uid) { firebaseUser ->
            val contacts: ArrayList<HashMap<String, String>> = arrayListOf()
            CoroutineScope(Dispatchers.IO).launch {
                if (firebaseUser == null) return@launch
                val currentUserContacts = firebaseUser.contacts
                currentUserContacts.forEach { uid ->
                    val profilePicture = storageRepository.getProfilePicture(uid)
                    val username = fireStoreRepository.getUsernameByUid(uid)
                    if (username != null) {
                        contacts.add(
                            hashMapOf(
                                "username" to username,
                                "profilePicture" to profilePicture.toString()
                            )
                        )
                    }
                }
                contactsResult(contacts)
            }
        }
    }

    fun setUserListener(listener: UserStateListener) = userRepository.setListener(listener)

    suspend fun getChatFlow(userName2: String, chat: (Chat?) -> Unit) {
        val userUid1 = userRepository.getUserUid() ?: return
        val userUid2 = fireStoreRepository.getUserUidByUsername(userName2) ?: return

        fireStoreRepository.getChatFlow(userUid1 = userUid1, userUid2 = userUid2) { chatResult ->
            chat(chatResult)
        }
    }

    fun addMessage(message: String, chat: Chat) {
        val senderUid = userRepository.getUserUid()
        senderUid?.let {
            chat.messages.add(Message(message = message, senderUid = senderUid))
        }
    }

    suspend fun addMessage(message: String, username2: String) {
        val senderUid = userRepository.getUserUid() ?: return
        val user2Uid = fireStoreRepository.getUserUidByUsername(username2) ?: return
        fireStoreRepository.addOneMessage(message, senderUid, user2Uid)
    }

    fun isMessageFromMe(message: Message): Boolean {
        val senderUid = message.senderUid
        val userUid = userRepository.getUserUid()
        return senderUid == userUid
    }

    suspend fun getUserChats(chats: (ArrayList<ChatsListItem>) -> Unit, error: (String) -> Unit) {
        val userUid = userRepository.getUserUid() ?: return
        fireStoreRepository.getUserChatsFlow(userUid = userUid, chat = { returnedChats ->
            CoroutineScope(Dispatchers.IO).launch {
                val customChats = arrayListOf<ChatsListItem>()

                returnedChats.forEach { chat ->
                    val otherUid =
                        if (chat.uid1 == userRepository.getUserUid()) chat.uid2 else chat.uid1
                    customChats.add(
                        ChatsListItem(
                            profilePicture = storageRepository.getProfilePicture(otherUid),
                            username = (fireStoreRepository.getUsernameByUid(otherUid) ?: ""),
                            lastMessage = if (chat.messages.isNotEmpty()) chat.messages.last() else Message()
                        )
                    )
                }
                chats(customChats)
            }
        },
            error = { error(it) }
        )
    }

    suspend fun getUserProfilePicture(username: String): Uri {
        val uid = fireStoreRepository.getUserUidByUsername(username) ?: return Uri.EMPTY
        return storageRepository.getProfilePicture(uid)
    }
}