package com.example.talksy.presentation.chatScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.navigation.ChatsNav
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ChatScreenStates,
    onEvent: (ChatScreenEvent) -> Unit,
    events: SharedFlow<ChatScreenEvent>,
    user2: String?
) {

    onEvent(ChatScreenEvent.SetUser2(user2))

    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ChatScreenEvent.GoBackClicked -> navController.popBackStack()
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = state.otherUsername)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(ChatScreenEvent.GoBackClicked)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                })
        }) { contentPadding ->
        contentPadding
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPrev() {
    ChatScreen(
        navController = rememberNavController(),
        state = ChatScreenStates(),
        onEvent = {},
        events = MutableSharedFlow<ChatScreenEvent>().asSharedFlow(),
        user2 = "Ron189"
    )
}