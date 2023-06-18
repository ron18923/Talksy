package com.example.talksy.data

import android.net.Uri
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
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
}