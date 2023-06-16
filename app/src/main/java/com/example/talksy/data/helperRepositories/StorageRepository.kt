package com.example.talksy.data.helperRepositories

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class StorageRepository {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    suspend fun putProfilePicture(
        uid: String,
        profilePicture: Uri,
        profilePictureUri: (Uri) -> Unit
    ) {
        val task = storageRef.child(uid).putFile(profilePicture).await()
        task.storage.downloadUrl.await()?.let {
            profilePictureUri(it)
        }
    }

    suspend fun deleteProfilePicture(uid: String, profilePictureDeleted: () -> Unit) {
        try {
            storageRef.child(uid).delete().await()
            profilePictureDeleted()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}