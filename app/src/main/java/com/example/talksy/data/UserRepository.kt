package com.example.talksy.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(private val auth: FirebaseAuth) {
    private var listener: UserStateListener? = null

    private val user = auth.currentUser

    init {
        auth.addAuthStateListener{
            listener?.onUserStateChanged()
        }
    }

    fun setListener(listener: UserStateListener) {
        this.listener = listener
    }

    suspend fun addNewUser(
        username: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()
            user?.updateProfile(profileUpdates)?.await()
            if (user == null) errorMessage("failed to create new user.")
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            errorMessage(e.message!!)
        }
    }

    suspend fun signInUser(email: String, password: String, errorMessage: (String) -> Unit) {
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            if (user == null) errorMessage("failed to Login.")
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            errorMessage(e.message!!)
        }
    }

    // TODO: remove this function as it's not good practice
    fun getUser(): FirebaseUser? {
        return user
    }

    fun signOut(){
        auth.signOut()
    }

    suspend fun updateUsername(username: String){
        val user = user ?: return
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(username).build()
        user.updateProfile(userProfileChangeRequest).await()
    }

    suspend fun updateProfilePicture(profilePicture: Uri){
        val user = user ?: return
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(profilePicture).build()
        user.updateProfile(userProfileChangeRequest).await()
    }

    fun resetPassword(){
        val user = user
        if(user != null){
            auth.sendPasswordResetEmail(user.email?: "")
        }
    }

    fun getUserUid(): String?{
        return user?.uid
    }
}

interface UserStateListener {
    fun onUserStateChanged()
}