package com.example.talksy.presentation.main.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
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
            CenterAlignedTopAppBar(title = {
                Text(
                    text = item.label
                )
            })
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column(modifier = modifier.fillMaxWidth(0.9f)) {
                if (state.chats.isEmpty()) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
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
                                            text = chat["username"] ?: "",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    },
                                    supportingContent = { Text(text = chat["lastMessage"] ?: "") },
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onEvent(ChatsEvent.ChatClicked(chat["username"] ?: ""))
                                        },
                                    leadingContent = {
                                        Box(
                                            modifier = modifier
                                                .size(60.dp)
                                                .aspectRatio(1f)
                                        ) {
                                            Image(
                                                modifier = modifier
                                                    .fillMaxSize(),
                                                imageVector = Icons.Default.AccountCircle,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                                                contentDescription = "profile picture empty"
                                            )
                                            Image(
                                                modifier = modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape),
                                                painter = rememberAsyncImagePainter(model = chat["profilePicture"]),
                                                contentDescription = "profile picture"
                                            )
                                        }
                                    })
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
            arrayListOf(
//                hashMapOf(
//                    "username" to "Ron189",
//                    "profilePicture" to "",
//                    "lastMessage" to "I am fine. And you?"
//                ),
//                hashMapOf(
//                    "username" to "Gal17",
//                    "profilePicture" to "",
//                    "lastMessage" to "Wow this is amazing"
//                )
            )
        ),
        onEvent = {},
        events = MutableSharedFlow<ChatsEvent>().asSharedFlow()
    )
}