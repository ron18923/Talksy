package com.example.talksy.data.user

import android.app.Application
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(private val auth: FirebaseAuth, private val appContext: Application) {

    suspend fun addNewUser(name: String, email: String, password: String): Boolean {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            user?.updateProfile(profileUpdates)?.await()
            user != null
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Toast.makeText(
                appContext,
                e.message,
                Toast.LENGTH_SHORT,
            ).show()
            return false
        }
    }

    suspend fun signInUser(email: String, password: String): Boolean {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            user != null
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Toast.makeText(
                appContext,
                e.message,
                Toast.LENGTH_SHORT,
            ).show()
            return false
        }
    }

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }
}