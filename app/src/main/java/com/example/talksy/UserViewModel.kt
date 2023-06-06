package com.example.talksy

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.talksy.data.user.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var usernameInput = mutableStateOf("")
    var emailInput = mutableStateOf("")
    var passwordInput = mutableStateOf("")

    fun addNewUser() = liveData {
        val isSuccess = userRepository.addNewUser(
            usernameInput.value,
            emailInput.value,
            passwordInput.value
        ){

        }
        emit(isSuccess)
    }

    fun signInUser() = liveData {
        val isSuccess = userRepository.signInUser(emailInput.value, passwordInput.value){}
        emit(isSuccess)
    }

    fun checkIfFieldsValid(
        name: String = "  ", //spaces in the string are to pass name length checking.
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ): Boolean {
        if (name.length < 2) {
            errorMessage("Name must be at-least 2 characters.")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage("Valid Email must be entered.")
            return false
        } else if (password.length < 8) {
            errorMessage("Password Length must be at-least 8 characters.")
            return false
        }
        return true
    }

    fun getUser(): FirebaseUser? {
        return userRepository.getUser()
    }

    fun isSignedIn(): Boolean {
        return getUser() != null
    }
}