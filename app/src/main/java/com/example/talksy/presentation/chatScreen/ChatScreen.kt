package com.example.talksy.presentation.chatScreen

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talksy.TalksyApp.Companion.TAG
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
    user2: String?,
    activity: Activity = LocalContext.current as Activity //added as a parameter so preview works.
) {
//    this code is for the keyboard to overlap the screen.
    activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    LaunchedEffect(key1 = true) {
        Log.d(TAG, "ChatScreen: in launched effect")
        user2?.let {
            onEvent(ChatScreenEvent.SetUser2(user2))
        }

        events.collectLatest { event ->
            when (event) {
                ChatScreenEvent.GoBackClicked -> navController.popBackStack()
                else -> {}
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = state.user2)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(ChatScreenEvent.GoBackClicked)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                })
        }) { innerPadding ->
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ConstraintLayout(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth(0.88f)
                    .fillMaxHeight(),
            ) {
                val (messages, input) = createRefs()
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .constrainAs(messages) {}
                ) {
                    state.messages.forEach { message ->
                        item {
                            Text(
                                text = message.message,
                                color = if (message.isItMe) Color.Green else Color.Blue
                            )
                        }
                    }
                }
                TextField(
                    modifier = modifier
                        .constrainAs(input) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth(),
                    value = state.inputText,
                    onValueChange = { onEvent(ChatScreenEvent.InputChange(it)) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPrev() {
    ChatScreen(
        navController = rememberNavController(),
        state = ChatScreenStates(
            messages = arrayListOf(
                MessageChatScreen("Hey!", true),
                MessageChatScreen("Hey, hdyd?", false),
                MessageChatScreen("Great, thanks for asking :)", true)
            )
        ),
        onEvent = {},
        events = MutableSharedFlow<ChatScreenEvent>().asSharedFlow(),
        user2 = "Ron189",
        activity = ActivityPreview
    )
}

object ActivityPreview : Activity()