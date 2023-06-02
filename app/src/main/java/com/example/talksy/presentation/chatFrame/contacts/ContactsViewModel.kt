package com.example.talksy.presentation.chatFrame.contacts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.login.LoginEvent
import com.example.talksy.presentation.login.LoginStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(ContactsStates())
    val state: State<ContactsStates> = _state

    private val _events = MutableSharedFlow<ContactsEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ContactsEvent) {
        when (event) {
            else -> {}
        }
    }
}