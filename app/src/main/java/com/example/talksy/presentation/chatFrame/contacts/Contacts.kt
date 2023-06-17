package com.example.talksy.presentation.chatFrame.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val focusManager = LocalFocusManager.current
    //to manage the focus of the search text field.
    val searchFocusRequester = remember { FocusRequester() }

    val state = contactsViewModelContainer.state
    val onEvent = contactsViewModelContainer.onEvent
    val events = contactsViewModelContainer.events

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ContactsEvent.SearchClose -> focusManager.clearFocus()
                else -> {}
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth(0.9f)) {
        TextField(
            value = state.searchInput,
            onValueChange = { onEvent(ContactsEvent.SearchEntered(it)) },
            placeholder = { Text(text = "Search by username") },
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(searchFocusRequester),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (state.searchInput != TextFieldValue("")) {
                    IconButton(
                        onClick = { onEvent(ContactsEvent.SearchClose) }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
        )
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
                state = ContactsStates(TextFieldValue("")),
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