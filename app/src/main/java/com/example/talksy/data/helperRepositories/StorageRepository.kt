package com.example.talksy.data.helperRepositories

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class StorageRepository {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    suspend fun putPicture(
        fileName: String,
        profilePicture: Uri,
    ): Uri {
        val task = storageRef.child(fileName).putFile(profilePicture).await()
        return task.storage.downloadUrl.await()
    }

    suspend fun deleteProfilePicture(uid: String, profilePictureDeleted: () -> Unit) {
        try {
            storageRef.child(uid).delete().await()
            profilePictureDeleted()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getProfilePicture(uid: String): Uri{
        return try {
            storageRef.child(uid).downloadUrl.await() ?: Uri.EMPTY
        } catch (e: Exception){
            e.printStackTrace()
            Uri.EMPTY
        }
    }
}