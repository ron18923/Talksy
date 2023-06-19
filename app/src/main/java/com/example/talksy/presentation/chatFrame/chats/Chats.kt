package com.example.talksy.presentation.chatFrame.chats

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Chats(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    chatsViewModelContainer: ChatsViewModelContainer
) {
    Text(text = "chats")
}