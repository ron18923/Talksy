package com.example.talksy.presentation.chatFrame.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditAttributes
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.ReadMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavOptionsBuilder
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.presentation.chatFrame.ChatFrame
import com.example.talksy.presentation.chatFrame.ChatFrameEvent
import com.example.talksy.presentation.chatFrame.ChatFrameStates
import com.example.talksy.presentation.chatFrame.chats.ChatsEvent
import com.example.talksy.presentation.chatFrame.chats.ChatsState
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModelContainer
import com.example.talksy.presentation.chatFrame.contacts.ContactsEvent
import com.example.talksy.presentation.chatFrame.contacts.ContactsStates
import com.example.talksy.presentation.chatFrame.contacts.ContactsViewModelContainer
import com.example.talksy.presentation.destinations.EditProfileDestination
import com.example.talksy.presentation.destinations.LoginDestination
import com.example.talksy.presentation.destinations.StartComposeDestination
import com.example.talksy.ui.theme.TalksyTheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    settingsViewModelContainer: SettingsViewModelContainer
) {
    val state = settingsViewModelContainer.state
    val onEvent = settingsViewModelContainer.onEvent
    val events = settingsViewModelContainer.events

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                // TODO: temporary solution
                SettingsEvent.SignOut -> {
                    navigator?.navigate(LoginDestination)
                }
                SettingsEvent.GoToEditProfile -> navigator?.navigate(EditProfileDestination)
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxWidth(0.9f),
    ) {
        Spacer(modifier = modifier.height(20.dp))

        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        ) {
            Row(modifier = modifier.fillMaxSize()) {
                Box(modifier = modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)) {
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
                            .padding(6.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
                        painter = rememberAsyncImagePainter(model = state.profilePicture),
                        contentDescription = "profile picture"
                    )

                }
                Column(
                    modifier = modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.username, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = state.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = modifier.height(25.dp))

        CustomTextButton(
            leadingIcon = Icons.Outlined.EditAttributes,
            text = "Edit Profile",
            onClick = { onEvent(SettingsEvent.GoToEditProfile) }
        )
        CustomTextButton(
            leadingIcon = Icons.Outlined.Block,
            text = "Blocked Users"
        )
        CustomTextButton(
            leadingIcon = Icons.Outlined.Delete,
            text = "Delete Account"
        )
        CustomTextButton(
            leadingIcon = Icons.Outlined.PrivacyTip,
            text = "Privacy Policy"
        )
        CustomTextButton(
            leadingIcon = Icons.Outlined.ReadMore,
            text = "Terms & Conditions"
        )
        CustomTextButton(
            leadingIcon = Icons.Outlined.Logout,
            text = "Logout",
            onClick = { onEvent(SettingsEvent.SignOut) }
        )
    }
}

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        onClick = { onClick() },
    ) {
        ConstraintLayout(modifier = modifier.fillMaxWidth()) {
            val (startIcon, centerText, endIcon) = createRefs()
            Icon(
                modifier = modifier.constrainAs(startIcon) {
                    centerVerticallyTo(parent)
                    start.linkTo(parent.start)
                },
                imageVector = leadingIcon,
                contentDescription = "leading icon",
            )
            Text(
                modifier = modifier
                    .padding(start = 8.dp)
                    .constrainAs(centerText) {
                        centerVerticallyTo(parent)
                        start.linkTo(startIcon.end)
                    },
                text = text
            )
            Icon(
                modifier = modifier.constrainAs(endIcon) {
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end)
                },
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "forward arrow"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPrev() {
    TalksyTheme(darkTheme = true) {
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