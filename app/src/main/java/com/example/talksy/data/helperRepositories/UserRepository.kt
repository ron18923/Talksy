package com.example.talksy.data.helperRepositories

import android.net.Uri
import android.util.Log
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.dataModels.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(private val auth: FirebaseAuth) {
    private var listener: UserStateListener? = null

    init {
        auth.addAuthStateListener{
            listener?.onUserStateChanged()
        }
    }

    fun setListener(listener: UserStateListener) {
        this.listener = listener
    }

    suspend fun addNewUser(
        userObject: User,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        try {
            val firebaseUser = auth.createUserWithEmailAndPassword(userObject.email, password).await().user
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(userObject.username).build()
            firebaseUser?.updateProfile(profileUpdates)?.await()
            if (firebaseUser == null) errorMessage("failed to create new user.")
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            errorMessage(e.message!!)
        }
    }

    suspend fun signInUser(email: String, password: String, errorMessage: (String) -> Unit) {
        Log.d(TAG, "signInUser: login")
        try {
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).await().user
            if (firebaseUser == null) errorMessage("failed to Login.")
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            errorMessage(e.message!!)
        }
    }

    // TODO: remove this function as it's not good practice
    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut(){
        auth.signOut()
    }

    suspend fun updateUsername(username: String){
        val user = auth.currentUser ?: return
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(username).build()
        user.updateProfile(userProfileChangeRequest).await()
    }

    suspend fun updateProfilePicture(profilePicture: Uri){
        val user = auth.currentUser ?: return
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(profilePicture).build()
        user.updateProfile(userProfileChangeRequest).await()
    }

    fun resetPassword(){
        val user = auth.currentUser
        if(user != null){
            auth.sendPasswordResetEmail(user.email?: "")
        }
    }

    fun getUserUid(): String?{
        return auth.currentUser?.uid
    }
}

interface UserStateListener {
    fun onUserStateChanged()
}