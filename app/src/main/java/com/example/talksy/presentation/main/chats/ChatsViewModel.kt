package com.example.talksy.presentation.main.chats

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
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

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                chats = mainRepository.getUserChats()
            )
            Log.d(TAG, ": ${_state.value.chats}")
        }
    }

    fun onEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.ChatClicked ->
                viewModelScope.launch {
                    _events.emit(ChatsEvent.ChatClicked(event.username))
                }
        }
    }
}