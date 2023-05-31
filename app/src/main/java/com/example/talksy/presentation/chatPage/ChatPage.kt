package com.example.talksy.presentation.chatPage

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.talksy.R
import com.example.talksy.UserViewModel
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {
    if (!userViewModel.isSignedIn()) {
        navigator?.navigate(OnBoardingDestination)
        return
    }

    val user = userViewModel.getUser()!!

    val navItems = listOf(
        BottomNavItem("Chats", painterResource(R.drawable.baseline_chat_24)),
        BottomNavItem("Contacts", painterResource(R.drawable.baseline_people_24)),
        BottomNavItem("Settings", painterResource(R.drawable.baseline_settings_24)),
    )
    var selectedNavItem by remember { mutableStateOf(0) }

    Scaffold(bottomBar = {
        NavigationBar() {
            //TODO implement it later correctly.
            navItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, "nav icon") },
                    label = { Text(item.title) },
                    selected = selectedNavItem == index,
                    onClick = { selectedNavItem = index }
                )
            }
        }
    },
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = navItems[selectedNavItem].title) })
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedNavItem) {
                0 -> Chats(modifier)
                1 -> Contacts(modifier)
                2 -> Settings(modifier, user)
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
fun Settings(modifier: Modifier = Modifier, user: FirebaseUser) {
    Column(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)) {
            Image(
                modifier = modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "profile picture"
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = user.displayName!!)
                Text(text = user.email!!)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPagePrev() {
}