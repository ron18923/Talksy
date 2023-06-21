package com.example.talksy.presentation.chatScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _state = mutableStateOf(ChatScreenStates())
    val state: State<ChatScreenStates> = _state

    private val _events = MutableSharedFlow<ChatScreenEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ChatScreenEvent){

    }

}