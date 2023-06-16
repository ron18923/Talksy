package com.example.talksy.data

import android.net.Uri
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
import com.google.firebase.auth.FirebaseUser

class MainRepository(val userRepository: UserRepository, val storageRepository: StorageRepository) {

    suspend fun addNewUser(
        user: User,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        userRepository.addNewUser(user, password, errorMessage)
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

    suspend fun updateProfilePicture(profilePicture: Uri) {
        userRepository.updateProfilePicture(profilePicture)
    }

    suspend fun deleteProfilePicture(uid: String, profilePictureDeleted: () -> Unit) {
        storageRepository.deleteProfilePicture(
            uid = uid,
            profilePictureDeleted = profilePictureDeleted
        )
    }
}