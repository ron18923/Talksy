package com.example.talksy

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    //TODO check about the 'remember' thing
    var nameInput = mutableStateOf("")
    var emailInput = mutableStateOf("")
    var passwordInput = mutableStateOf("")

    fun addNewUser() {
        userRepository.addNewUser(nameInput.value, emailInput.value, passwordInput.value)
    }

    fun loginUser() {
        userRepository.loginUser(emailInput.value, passwordInput.value)
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
}