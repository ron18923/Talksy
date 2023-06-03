package com.example.talksy.presentation.chatFrame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.talksy.R
import com.example.talksy.presentation.chatFrame.chats.ChatsEvent
import com.example.talksy.presentation.chatFrame.chats.ChatsState
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModelContainer
import com.example.talksy.presentation.chatFrame.contacts.ContactsEvent
import com.example.talksy.presentation.chatFrame.contacts.ContactsStates
import com.example.talksy.presentation.chatFrame.contacts.ContactsViewModelContainer
import com.example.talksy.presentation.chatFrame.settings.Settings
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModelContainer
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.example.talksy.ui.theme.TalksyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ChatFrame(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    state: ChatFrameStates,
    onEvent: (ChatFrameEvent) -> Unit,
    events: SharedFlow<ChatFrameEvent>,
    chatsViewModelContainer: ChatsViewModelContainer,
    contactsViewModelContainer: ContactsViewModelContainer,
    settingsViewModelContainer: SettingsViewModelContainer
) {

    val navItems = listOf(
        BottomNavItem("Chats", painterResource(R.drawable.baseline_chat_24)),
        BottomNavItem("Contacts", painterResource(R.drawable.baseline_people_24)),
        BottomNavItem("Settings", painterResource(R.drawable.baseline_settings_24)),
    )

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ChatFrameEvent.NoUserFound -> {
                    navigator?.navigate(OnBoardingDestination)
                }

                else -> {}
            }
        }
    }

    Scaffold(bottomBar = {
        NavigationBar() {
            //TODO implement it later correctly.
            navItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, "nav icon") },
                    label = { Text(item.title) },
                    selected = state.selectedNavItem == index,
                    onClick = {
                        onEvent(ChatFrameEvent.NavItemSelected(index))
                    }
                )
            }
        }
    },
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = navItems[state.selectedNavItem].title) })
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (state.selectedNavItem) {
                0 -> Chats(modifier, chatsViewModelContainer)
                1 -> Contacts(modifier, contactsViewModelContainer)
                2 -> Settings(modifier, settingsViewModelContainer)
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: Painter
)

@Composable
fun Chats(
    modifier: Modifier = Modifier,
    chatsViewModelContainer: ChatsViewModelContainer
) {
    Text(text = "chats")
}

@Composable
fun Contacts(
    modifier: Modifier = Modifier,
    contactsViewModelContainer: ContactsViewModelContainer
) {
    Text(text = "contacts")
}

@Preview(showBackground = true)
@Composable
fun ChatFramePrev() {
    TalksyTheme {
        ChatFrame(
            navigator = null,
            state = ChatFrameStates(2),
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