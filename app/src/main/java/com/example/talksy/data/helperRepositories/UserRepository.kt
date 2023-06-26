package com.example.talksy.data.helperRepositories

import android.net.Uri
import android.util.Log
import com.example.talksy.TalksyApp.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(private val auth: FirebaseAuth) {
    private var listeners: ArrayList<UserStateListener?> = arrayListOf()

    init {
        auth.addAuthStateListener{
            listeners.forEach { listener ->
                listener?.onUserStateChanged()
            }
        }
    }

    fun setListener(listener: UserStateListener) {
        listeners.add(listener)
    }

    suspend fun addNewUser(
        username: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ): String? {
        try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()
            firebaseUser?.updateProfile(profileUpdates)?.await()
            if (firebaseUser == null){
                errorMessage("failed to create new user.")
                return null
            }
            return firebaseUser.uid
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            errorMessage(e.message!!)
            return null
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

    fun signOutUser(){
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