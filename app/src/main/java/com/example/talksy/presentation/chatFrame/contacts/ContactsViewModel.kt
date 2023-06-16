package com.example.talksy.presentation.chatFrame.contacts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val fireStoreRepository: FireStoreRepository
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

data class ContactsViewModelContainer(
    var state: ContactsStates,
    var onEvent: (ContactsEvent) -> Unit,
    var events: SharedFlow<ContactsEvent>,
)