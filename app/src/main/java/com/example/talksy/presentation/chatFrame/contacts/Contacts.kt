package com.example.talksy.presentation.chatFrame.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.presentation.chatFrame.ChatFrame
import com.example.talksy.presentation.chatFrame.ChatFrameEvent
import com.example.talksy.presentation.chatFrame.ChatFrameStates
import com.example.talksy.presentation.chatFrame.chats.ChatsEvent
import com.example.talksy.presentation.chatFrame.chats.ChatsState
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModelContainer
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModelContainer
import com.example.talksy.ui.theme.TalksyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(
    modifier: Modifier = Modifier,
    navController: NavController,
    contactsViewModelContainer: ContactsViewModelContainer,
) {
    val state = contactsViewModelContainer.state
    val onEvent = contactsViewModelContainer.onEvent
    val events = contactsViewModelContainer.events

    var searchActive by remember { mutableStateOf(false) }

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ContactsEvent.SearchClose -> {
                    searchActive = false
                }

                else -> {}
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth(0.9f)) {
        SearchBar(
            modifier = modifier.fillMaxWidth(),
            query = state.searchInput,
            onQueryChange = { onEvent(ContactsEvent.SearchEntered(it)) },
            onSearch = { /* TODO: implement later */ },
            active = searchActive,
            onActiveChange = { searchActive = it },
            placeholder = { Text(text = "Search by username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search icon"
                )
            },
            trailingIcon = {
                if (searchActive) {
                    Icon(
                        modifier = modifier.clickable { onEvent(ContactsEvent.SearchClose) },
                        imageVector = Icons.Default.Close, contentDescription = "Close icon",
                    )
                }
            }
        ) {
            state.searchList.forEach { username ->
                Row(
                    modifier = modifier
                        .padding(14.dp)
                        .fillMaxWidth()
                        .clickable { onEvent(ContactsEvent.AddNewContact(username)) }
                ) {
                    Icon(
                        modifier = modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon"
                    )
                    Text(text = username)
                }
            }
        }
        Spacer(modifier = modifier.height(20.dp))
        LazyColumn {
            state.contactsList.forEach { contact ->
                item {
                    ListItem(
                        modifier = modifier.fillMaxWidth(),
                        headlineContent = { Text(text = contact["username"] ?: "") },
                        leadingContent = {
                            Box(
                                modifier = modifier
                                    .size(40.dp)
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
                                    painter = rememberAsyncImagePainter(model = contact["profilePicture"]),
                                    contentDescription = "profile picture"
                                )
                            }
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsPrev() {
    TalksyTheme(darkTheme = true) {
        ChatFrame(
            navController = rememberNavController(),
            state = ChatFrameStates(1),
            onEvent = {},
            events = MutableSharedFlow<ChatFrameEvent>().asSharedFlow(),
            chatsViewModelContainer = ChatsViewModelContainer(
                state = ChatsState(""),
                onEvent = {},
                events = MutableSharedFlow<ChatsEvent>().asSharedFlow()
            ),
            contactsViewModelContainer = ContactsViewModelContainer(
                state = ContactsStates(
                    contactsList = arrayListOf(
                        hashMapOf(
                            "username" to "Ron189",
                            "profilePicture" to "xcVdQX0fgMSYEiK6LDamj8uBk7Z2"
                        ),
                        hashMapOf(
                            "username" to "Gal1710",
                            "profilePicture" to "xcVdQX0fgMSYEiK6LDamj8uBk7Z2"
                        ),
                        hashMapOf(
                            "username" to "Hello111",
                            "profilePicture" to "xcVdQX0fgMSYEiK6LDamj8uBk7Z2"
                        )
                    )
                ),
                onEvent = {},
                events = MutableSharedFlow<ContactsEvent>().asSharedFlow()
            ),
            settingsViewModelContainer = SettingsViewModelContainer(
                state = SettingsStates("Ron", "ronron18923@gmail.com"),
                onEvent = {},
                events = MutableSharedFlow<SettingsEvent>().asSharedFlow()

            )
        )
    }
}