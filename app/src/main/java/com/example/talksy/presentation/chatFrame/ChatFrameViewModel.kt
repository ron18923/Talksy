package com.example.talksy.presentation.chatFrame

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatFrameViewModel @Inject constructor(): ViewModel() {

    private val _state = mutableStateOf(ChatFrameStates())
    val state: State<ChatFrameStates> = _state

    private val _events = MutableSharedFlow<ChatFrameEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ChatFrameEvent) {
        when (event) {
            ChatFrameEvent.NoUserFound -> {
                viewModelScope.launch {
                    _events.emit(
                        ChatFrameEvent.NoUserFound
                    )
                }
            }

            is ChatFrameEvent.NavItemSelected -> {
                _state.value = _state.value.copy(selectedNavItem = event.value)
            }
        }
    }
}