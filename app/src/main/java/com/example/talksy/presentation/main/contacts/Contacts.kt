package com.example.talksy.presentation.main.contacts

import android.util.Log
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.navigation.ChatsNav
import com.example.talksy.presentation.navigation.GraphIconLabel
import com.example.talksy.ui.theme.TalksyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ContactsStates,
    onEvent: (ContactsEvent) -> Unit,
    events: SharedFlow<ContactsEvent>
) {

    var searchActive by remember { mutableStateOf(false) }

    val navItems = listOf(
        GraphIconLabel.Chats,
        GraphIconLabel.Contacts,
        GraphIconLabel.Settings
    )
    val item = GraphIconLabel.Contacts

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ContactsEvent.SearchClose -> {
                    searchActive = false
                }
                is ContactsEvent.ExistingContactClicked -> {
                    navController.navigate("${ChatsNav.ChatScreen.route}/${event.username}")
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
                Log.d(TAG, "Contacts: ${state.searchInput}")
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
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close icon",
                            )
                        }
                    }
                ) {
                    state.searchList.forEach { username ->
                        Row(
                            modifier = modifier
                                .padding(14.dp)
                                .fillMaxWidth()
                                .clickable { onEvent(ContactsEvent.NewContactClicked(username)) }
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
                                headlineContent = { Text(text = contact["username"] ?: "") },
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onEvent(ContactsEvent.ExistingContactClicked(contact["username"]))
                                    },
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
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsPrev() {
    TalksyTheme(darkTheme = true) {
        Contacts(
            navController = rememberNavController(),
            state = ContactsStates(
                contactsList = arrayListOf(
                    hashMapOf(
                        "username" to "Ron189",
                        "profilePicture" to ""
                    ),
                    hashMapOf(
                        "username" to "Gal17",
                        "profilePicture" to "",
                    )
                )
            ),
            onEvent = {},
            events = MutableSharedFlow<ContactsEvent>().asSharedFlow(),
        )
    }
}