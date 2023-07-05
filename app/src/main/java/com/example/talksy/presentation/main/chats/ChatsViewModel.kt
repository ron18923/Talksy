package com.example.talksy.presentation.main.chats

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.MainRepository
import com.example.talksy.data.dataModels.ChatsListItem
import com.example.talksy.data.dataModels.ChatsListItemView
import com.example.talksy.data.dataModels.MessageView
import com.example.talksy.data.helperRepositories.UserStateListener
import com.example.talksy.presentation.reusable.nonComposables.convertLongToTime
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
        init()
        mainRepository.setUserListener(_userStateListener)
    }

    private fun init() {
        _state.value =
            _state.value.copy(chats = arrayListOf()) //emptying chats list before assigning
        viewModelScope.launch {
            mainRepository.getUserChats(chats = { chats ->
                val sortedChats = chats.sortedWith(compareByDescending { it.lastMessage.timestamp })
                _state.value = _state.value.copy(
                    chats = chatListItemConverter(ArrayList(sortedChats)),
                    showProgressBar = false //indicating to the progress bar that data has been loaded.
                )
            },
                error = { onEvent(ChatsEvent.ShowError(it)) }
            )
        }
    }

    fun onEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.ChatClicked -> viewModelScope.launch {
                _events.emit(ChatsEvent.ChatClicked(event.username))
            }

            ChatsEvent.Dispose -> _state.value = _state.value.copy(chats = arrayListOf())

            is ChatsEvent.ShowError -> viewModelScope.launch {
                _events.emit(ChatsEvent.ShowError(event.error))
            }
        }
    }

    inner class UserStateListenerImpl : UserStateListener {
        override fun onUserStateChanged() {
            init()
        }

    }

    private fun chatListItemConverter(chatListItems: ArrayList<ChatsListItem>): ArrayList<ChatsListItemView> {
        val newChatListItems = arrayListOf<ChatsListItemView>()

        chatListItems.forEach { chatListItem ->
            val isItMe = mainRepository.isMessageFromMe(chatListItem.lastMessage)
            newChatListItems.add(
                ChatsListItemView(
                    profilePicture = chatListItem.profilePicture,
                    username = chatListItem.username,
                    lastMessage = MessageView(
                        message = chatListItem.lastMessage.message,
                        timestamp = convertLongToTime(chatListItem.lastMessage.timestamp),
                        isItMe = isItMe
                    )
                )
            )
        }
        return newChatListItems
    }
}