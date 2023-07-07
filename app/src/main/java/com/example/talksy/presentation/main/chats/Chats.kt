package com.example.talksy.presentation.main.chats

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.dataModels.ChatsListItemView
import com.example.talksy.data.dataModels.MessageView
import com.example.talksy.presentation.navigation.ChatsNav
import com.example.talksy.presentation.navigation.GraphIconLabel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chats(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ChatsState,
    onEvent: (ChatsEvent) -> Unit,
    events: SharedFlow<ChatsEvent>
) {

    val navItems = listOf(
        GraphIconLabel.Chats,
        GraphIconLabel.Contacts,
        GraphIconLabel.Settings
    )

    val item = GraphIconLabel.Chats

//    DisposableEffect(Unit){
//        onDispose {
//            onEvent(ChatsEvent.Dispose)
//        }
//    }

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is ChatsEvent.ChatClicked ->
                    navController.navigate("${ChatsNav.ChatScreen.route}/${event.username}")

                is ChatsEvent.ShowError -> { // TODO: show error on screen
                }

                else -> {}
            }
        }
    }

    Scaffold(bottomBar = {
        NavigationBar() {
            navItems.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "navigation item icon"
                        )
                    },
                    label = { Text(text = screen.label) },
                    selected = screen.route == item.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    })
            }
        }
    },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = item.label
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            if (state.showProgressBar)
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            else {
                Column(
                    modifier = modifier.fillMaxWidth(0.9f).align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.Top
                ) {
                    if (state.chats.isEmpty()) {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You don't have chats right now. Let's change that!",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn {
                            state.chats.forEach { chat ->
                                item {
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = chat.username,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        },
                                        supportingContent = {
                                            Row(
                                                modifier = modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text =
                                                    if (chat.lastMessage.isItMe) "You: "
                                                    else {
                                                        ""
                                                    } + chat.lastMessage.message,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = chat.lastMessage.timestamp,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        },
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onEvent(ChatsEvent.ChatClicked(chat.username))
                                            },
                                        leadingContent = {
                                            Box(
                                                modifier = modifier
                                                    .size(52.dp)
                                                    .aspectRatio(1f)
                                            ) {
                                                Icon(
                                                    modifier = modifier
                                                        .fillMaxSize(),
                                                    imageVector = Icons.Default.AccountCircle,
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    contentDescription = "profile picture empty"
                                                )
                                                Image(
                                                    modifier = modifier
                                                        .fillMaxSize()
                                                        .clip(CircleShape),
                                                    painter = rememberAsyncImagePainter(model = chat.profilePicture),
                                                    contentScale = ContentScale.Crop,
                                                    contentDescription = "profile picture",
                                                )
                                            }
                                        })
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatsPrev() {
    Chats(
        navController = rememberNavController(),
        state = ChatsState(
            chats = arrayListOf(
                ChatsListItemView(
                    username = "Ron189",
                    lastMessage = MessageView(
                        message = "Thank you :)",
                        timestamp = "17:52",
                        isItMe = false
                    )
                ),
                ChatsListItemView(
                    username = "Gal17",
                    lastMessage = MessageView(
                        message = "Great! Okay.",
                        timestamp = "08:13",
                        isItMe = true
                    )
                )
            ),
            showProgressBar = true
        ),
        onEvent = {},
        events = MutableSharedFlow<ChatsEvent>().asSharedFlow()
    )
}