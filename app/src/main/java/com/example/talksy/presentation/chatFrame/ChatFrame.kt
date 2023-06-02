package com.example.talksy.presentation.chatFrame

import com.example.talksy.TalksyApp.Companion.EVENTS
import com.example.talksy.TalksyApp.Companion.ONEVENT
import com.example.talksy.TalksyApp.Companion.STATE
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.talksy.R
import com.example.talksy.TalksyApp
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.chatFrame.chats.ChatsEvent
import com.example.talksy.presentation.chatFrame.chats.ChatsState
import com.example.talksy.presentation.chatFrame.contacts.ContactsEvent
import com.example.talksy.presentation.chatFrame.contacts.ContactsStates
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsStates
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModel
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModelContainer
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.example.talksy.presentation.login.Login
import com.example.talksy.presentation.login.LoginEvent
import com.example.talksy.presentation.login.LoginStates
import com.example.talksy.presentation.register.RegisterEvent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ChatFrame(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    state: ChatFrameStates,
    onEvent: (ChatFrameEvent) -> Unit,
    events: SharedFlow<ChatFrameEvent>,
    chatsViewModelMap: Map<String, Any>,
    contactsViewModelMap: Map<String, Any>,
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
                0 -> Chats(modifier)
                1 -> Contacts(modifier)
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
fun Chats(modifier: Modifier = Modifier) {
    Text(text = "chats")
}

@Composable
fun Contacts(modifier: Modifier = Modifier) {
    Text(text = "contacts")
}

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    settingsViewModelContainer: SettingsViewModelContainer
) {
    val state = settingsViewModelContainer.state
    val onEvent = settingsViewModelContainer.onEvent
    val events = settingsViewModelContainer.events

    Column(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        ) {
            Image(
                modifier = modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "profile picture"
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = state.displayName)
                Text(text = state.email)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatFramePrev() {

    ChatFrame(
        navigator = null,
        state = ChatFrameStates(2),
        onEvent = {},
        events = MutableSharedFlow<ChatFrameEvent>().asSharedFlow(),
        chatsViewModelMap = mapOf(
            STATE to ChatsState(""),
            ONEVENT to {},
            EVENTS to MutableSharedFlow<ChatsEvent>().asSharedFlow()
        ),
        contactsViewModelMap = mapOf(
            STATE to ContactsStates(""),
            ONEVENT to {},
            ONEVENT to {},
            EVENTS to MutableSharedFlow<ContactsEvent>().asSharedFlow()
        ),
        settingsViewModelContainer = SettingsViewModelContainer(
            state = SettingsStates("Ron", "ronronr18923@gmail.com"),
            onEvent = {},
            events = MutableSharedFlow<SettingsEvent>().asSharedFlow()

        )
    )
}