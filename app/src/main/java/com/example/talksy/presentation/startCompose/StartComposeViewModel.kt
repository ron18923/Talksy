package com.example.talksy.presentation.startCompose

import androidx.lifecycle.ViewModel
import com.example.talksy.data.user.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartComposeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getUser(): FirebaseUser? {
        return userRepository.getUser()
    }

}