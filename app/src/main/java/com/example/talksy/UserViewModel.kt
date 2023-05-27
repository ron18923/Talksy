package com.example.talksy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun addNewUser(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addNewUser(name, email, password)
        }
    }
}