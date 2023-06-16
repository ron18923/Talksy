package com.example.talksy

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.talksy.data.MainRepository
import com.example.talksy.data.dataModels.User
import com.example.talksy.data.helperRepositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var usernameInput by mutableStateOf("")
    var emailInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")

    fun addNewUser() = liveData {
        val isSuccess = mainRepository.addNewUser(
            User(username = usernameInput, email = emailInput),
            passwordInput
        ){

        }
        emit(isSuccess)
    }

    fun signInUser() = liveData {
        val isSuccess = mainRepository.signInUser(emailInput, passwordInput){}
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
        return mainRepository.getUser()
    }

    fun isSignedIn(): Boolean {
        return getUser() != null
    }
}