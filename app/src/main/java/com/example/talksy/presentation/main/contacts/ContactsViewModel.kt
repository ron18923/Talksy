package com.example.talksy.presentation.main.contacts

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.UserStateListener
import com.example.talksy.presentation.main.settings.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(ContactsStates())
    val state: State<ContactsStates> = _state

    private val _events = MutableSharedFlow<ContactsEvent>()
    val events = _events.asSharedFlow()

    private val _userStateListener = UserStateListenerImpl()

    init {
        init()
        mainRepository.setUserListener(_userStateListener)
    }

    private fun init() {
        viewModelScope.launch {
            mainRepository.getUserContacts { contacts ->
                _state.value = _state.value.copy(contactsList = contacts)
            }
        }
    }

    fun onEvent(event: ContactsEvent) {
        when (event) {
            ContactsEvent.SearchClose -> {
                if (state.value.searchInput.isNotEmpty()) {
                    _state.value = _state.value.copy(searchInput = "")
                    return
                }
                viewModelScope.launch {
                    _events.emit(
                        ContactsEvent.SearchClose
                    )
                }
            }


            is ContactsEvent.SearchEntered -> {
                _state.value = _state.value.copy(searchInput = event.value)
                if (state.value.searchInput.isNotEmpty()) {
                    viewModelScope.launch {
                        val searchResult = mainRepository.searchUsers(event.value)
                        _state.value = _state.value.copy(searchList = searchResult)
                    }
                } else {
                    _state.value = _state.value.copy(searchList = arrayListOf())
                }
            }

            is ContactsEvent.NewContactClicked -> {
                viewModelScope.launch {
                    mainRepository.addNewContact(event.username)
                }
            }

            is ContactsEvent.ExistingContactClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        ContactsEvent.ExistingContactClicked(event.username)
                    )
                }
            }

            ContactsEvent.Dispose -> _state.value = _state.value.copy(
                searchInput = "",
                searchList = arrayListOf(),
                contactsList = arrayListOf()
            )
        }
    }

    inner class UserStateListenerImpl : UserStateListener {
        override fun onUserStateChanged() {
            _state.value = _state.value.copy(
                searchInput = "",
                searchList = arrayListOf(),
                contactsList = arrayListOf()
            )
            init()
        }
    }
}