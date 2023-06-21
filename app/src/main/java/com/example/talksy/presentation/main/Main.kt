package com.example.talksy.presentation.main

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.talksy.presentation.graphs.navigation.BottomNavScreen
import com.example.talksy.presentation.graphs.navigation.Graph
import com.example.talksy.presentation.graphs.navigation.GraphIconLabel
import com.example.talksy.presentation.graphs.navigation.MainNav
import com.example.talksy.ui.theme.TalksyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: MainStates,
    onEvent: (MainEvent) -> Unit,
    events: SharedFlow<MainEvent>,
) {
    val navItems = listOf(
        GraphIconLabel.Chats,
        GraphIconLabel.Contacts,
        GraphIconLabel.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = navItems.any { it.route == currentDestination?.route }

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                MainEvent.NoUserFound -> {
                    navController.navigate(Graph.Authentication.route)
                }

                else -> {}
            }
        }
    }

    Scaffold(bottomBar = {
        if (bottomBarDestination) {
            NavigationBar() {
                //TODO implement it later correctly.

                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = "navigation item icon"
                            )
                        },
                        label = { Text(text = screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
        }
    },
        topBar = {
            if (bottomBarDestination) {
                CenterAlignedTopAppBar(title = {
                    Text(
                        text = /* todo fix later */currentDestination?.label?.toString() ?: "Label"
                    )
                })
            }
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MainNav(navController = navController)
//            when (state.selectedNavItem) {
//                0 -> Chats(modifier, navController, chatsViewModelContainer)
//                1 -> Contacts(modifier, navController,  contactsViewModelContainer)
//                2 -> Settings(modifier, navController, settingsViewModelContainer)
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatFramePrev() {
    TalksyTheme {
        Main(
            navController = rememberNavController(),
            state = MainStates(2),
            onEvent = {},
            events = MutableSharedFlow<MainEvent>().asSharedFlow(),
//            chatsViewModelContainer = ChatsViewModelContainer(
//                state = ChatsState(""),
//                onEvent = {},
//                events = MutableSharedFlow<ChatsEvent>().asSharedFlow()
//            ),
//            contactsViewModelContainer = ContactsViewModelContainer(
//                state = ContactsStates(""),
//                onEvent = {},
//                events = MutableSharedFlow<ContactsEvent>().asSharedFlow()
//            ),
//            settingsViewModelContainer = SettingsViewModelContainer(
//                state = SettingsStates("Ron", "ronron18923@gmail.com"),
//                onEvent = {},
//                events = MutableSharedFlow<SettingsEvent>().asSharedFlow()
//
//            )
        )
    }
}