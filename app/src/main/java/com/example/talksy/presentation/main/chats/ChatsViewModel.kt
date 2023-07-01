package com.example.talksy.presentation.main.chats

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
class ChatsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(ChatsState())
    val state: State<ChatsState> = _state

    private val _events = MutableSharedFlow<ChatsEvent>()
    val events = _events.asSharedFlow()

    private val _userStateListener = UserStateListenerImpl()

    init {
        viewModelScope.launch {
            mainRepository.getUserChats { chats ->
                _state.value = _state.value.copy(
                    chats = chats
                )
            }
        }
        mainRepository.setUserListener(_userStateListener)
    }

    fun onEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.ChatClicked ->
                viewModelScope.launch {
                    _events.emit(ChatsEvent.ChatClicked(event.username))
                }

            ChatsEvent.Dispose -> _state.value = _state.value.copy(chats = arrayListOf())
        }
    }

    inner class UserStateListenerImpl : UserStateListener {
        override fun onUserStateChanged() {
            _state.value = _state.value.copy(chats = arrayListOf())
            viewModelScope.launch {
                mainRepository.getUserChats { chats ->
                    _state.value = _state.value.copy(
                        chats = chats
                    )
                }
            }
        }

    }
}