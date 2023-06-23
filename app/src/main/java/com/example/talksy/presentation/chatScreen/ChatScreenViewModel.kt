package com.example.talksy.presentation.chatScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.MainRepository
import com.example.talksy.presentation.login.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(ChatScreenStates())
    val state: State<ChatScreenStates> = _state

    private val _events = MutableSharedFlow<ChatScreenEvent>()
    val events = _events.asSharedFlow()

    init {

    }

    fun onEvent(event: ChatScreenEvent){
        when (event) {
            ChatScreenEvent.GoBackClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        ChatScreenEvent.GoBackClicked
                    )
                }
            }
        }
        }

}