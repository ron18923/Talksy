package com.example.talksy.data.user

import android.app.Application
import android.util.Log
import com.example.talksy.TalksyApp.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(private val auth: FirebaseAuth) {

    suspend fun addNewUser(
        name: String,
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ) {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
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

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut(){
        auth.signOut()
    }
}