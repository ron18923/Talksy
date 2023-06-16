package com.example.talksy.presentation.chatFrame.chats

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.talksy.data.helperRepositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(ChatsState())
    val state: State<ChatsState> = _state

    private val _events = MutableSharedFlow<ChatsEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ChatsEvent) {
        when (event) {
            else -> {}
        }
    }
}

data class ChatsViewModelContainer(
    var state: ChatsState,
    var onEvent: (ChatsEvent) -> Unit,
    var events: SharedFlow<ChatsEvent>,
)