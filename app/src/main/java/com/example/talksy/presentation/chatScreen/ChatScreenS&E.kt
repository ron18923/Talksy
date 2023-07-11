package com.example.talksy.presentation.chatScreen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.talksy.data.dataModels.MessageView

sealed class ChatScreenEvent() {
    data class InputChange(val input: String) : ChatScreenEvent()
    data class SetUser2(val user2: String) : ChatScreenEvent()
    data class ClipImagePicked(val uri: Uri) : ChatScreenEvent()
    data class EmojiClicked(val emoji: String) : ChatScreenEvent()

    object GoBackClicked : ChatScreenEvent()
    object EmojisClicked : ChatScreenEvent()
    object ClipClicked : ChatScreenEvent()
    object SendClicked : ChatScreenEvent()

    object Dispose : ChatScreenEvent()
}

data class ChatScreenStates(
    val otherProfile: Uri = Uri.EMPTY,
    val user2: String = "",
    val inputText: String = "",
    val messages: ArrayList<MessageView> = arrayListOf(),
    val showProgressBar: Boolean = true,
    var showImagePicker: Boolean = false,
    var showSendingDialog: Boolean = false
)
