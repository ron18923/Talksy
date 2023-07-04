package com.example.talksy.presentation.chatScreen

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.data.MainRepository
import com.example.talksy.data.dataModels.Message
import com.example.talksy.data.dataModels.MessageView
import com.example.talksy.presentation.reusable.nonComposables.convertLongToTime
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

    private fun manualInit() {
        viewModelScope.launch {
            val username = state.value.user2
            val profilePicture = mainRepository.getUserProfilePicture(username)
            mainRepository.getChatFlow(userName2 = username) { chat ->
                if (chat == null) return@getChatFlow
                val updatedMessages = messagesConverter(chat.messages)
                _state.value = _state.value.copy(messages = updatedMessages, otherProfile = profilePicture)
            }
        }
    }

    fun onEvent(event: ChatScreenEvent) {
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

            ChatScreenEvent.SendClicked -> {
                val message = _state.value.inputText
                _state.value = _state.value.copy(inputText = "")
                viewModelScope.launch {
                    mainRepository.addMessage(
                        message = message,
                        username2 = _state.value.user2
                    )
                }
            }

            ChatScreenEvent.Dispose -> _state.value = _state.value.copy(
                otherProfile = Uri.EMPTY,
                user2 = "",
                inputText = "",
                messages = arrayListOf()
            )
        }
    }

    private fun messagesConverter(messages: ArrayList<Message>): ArrayList<MessageView> {
        val newMessages = arrayListOf<MessageView>()

        messages.forEach { message ->
            val isFromMe = mainRepository.isMessageFromMe(message)
            newMessages.add(
                MessageView(
                    message = message.message,
                    timestamp = convertLongToTime(message.timestamp),
                    isItMe = isFromMe
                )
            )
        }
        return newMessages
    }
}