package com.example.talksy.presentation.chatScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.data.dataModels.Message
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

    private fun manualInit(){
        viewModelScope.launch {
            mainRepository.getChat(userName2 = state.value.user2) { chat ->
                if(chat == null) return@getChat
                val updatedMessages = messageConverter(chat.messages)
                _state.value = _state.value.copy(messages = updatedMessages)
            }
        }
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

            is ChatScreenEvent.SetUser2 -> {
                _state.value = _state.value.copy(user2 = event.user2)
                manualInit()
            }

            is ChatScreenEvent.InputChange -> {
                _state.value = _state.value.copy(inputText = event.input)
            }
        }
        }

    private fun messageConverter(messages: ArrayList<Message>): ArrayList<MessageChatScreen>{
        val newMessages = arrayListOf<MessageChatScreen>()

        messages.forEach { message ->
            val isFromMe = mainRepository.isMessageFromMe(message)
            newMessages.add(MessageChatScreen(message.message, isItMe = isFromMe))
        }
        return newMessages
    }
}