package com.example.talksy.data

import android.net.Uri
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import com.google.firebase.auth.FirebaseUser

class MainRepository(
    val userRepository: UserRepository,
    val storageRepository: StorageRepository,
    val fireStoreRepository: FireStoreRepository
) {

    suspend fun updateProfilePicture(profilePicture: Uri) {
        userRepository.updateProfilePicture(profilePicture)
    }

    suspend fun signInUser(email: String, password: String, errorMessage: (String) -> Unit) {
        userRepository.signInUser(email, password, errorMessage)
    }

    fun signOutUser(){
        userRepository.signOutUser()
    }

    fun getUser(): FirebaseUser? {
        return userRepository.getUser()
    }

    fun resetPassword() {
        userRepository.resetPassword()
    }

    fun getUserUid(): String? {
        return userRepository.getUserUid()
    }

    suspend fun addNewUser(
        username: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        if(!fireStoreRepository.checkUsername(username, errorMessage)) return
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
    ) {
        storageRepository.putProfilePicture(
            uid = uid,
            profilePicture = profilePicture
        ) { resultUri ->
            profilePictureUri(resultUri)
        }
    }

    suspend fun deleteProfilePicture(uid: String, profilePictureDeleted: () -> Unit) {
        storageRepository.deleteProfilePicture(
            uid = uid,
            profilePictureDeleted = profilePictureDeleted
        )
    }

    suspend fun searchUsers(searchUsers: String): ArrayList<String> {
        return fireStoreRepository.searchUsers(searchUsers)
    }

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

    fun setUserListener(listener: UserStateListener) {
        userRepository.setListener(listener)
    }
}