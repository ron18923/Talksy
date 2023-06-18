package com.example.talksy.presentation.chatFrame.contacts

import android.view.SearchEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talksy.presentation.chatFrame.ChatFrame
import com.example.talksy.presentation.chatFrame.ChatFrameEvent
import com.example.talksy.presentation.chatFrame.ChatFrameStates
import com.example.talksy.presentation.chatFrame.chats.ChatsEvent
import com.example.talksy.presentation.chatFrame.chats.ChatsState
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModelContainer
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModelContainer
import com.example.talksy.presentation.destinations.EditProfileDestination
import com.example.talksy.presentation.destinations.StartComposeDestination
import com.example.talksy.ui.theme.TalksyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(
    modifier: Modifier = Modifier,
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
    }

}

@Preview(showBackground = true)
@Composable
fun ContactsPrev() {
    TalksyTheme(darkTheme = true) {
        ChatFrame(
            navigator = null,
            state = ChatFrameStates(1),
            onEvent = {},
            events = MutableSharedFlow<ChatFrameEvent>().asSharedFlow(),
            chatsViewModelContainer = ChatsViewModelContainer(
                state = ChatsState(""),
                onEvent = {},
                events = MutableSharedFlow<ChatsEvent>().asSharedFlow()
            ),
            contactsViewModelContainer = ContactsViewModelContainer(
                state = ContactsStates(""),
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