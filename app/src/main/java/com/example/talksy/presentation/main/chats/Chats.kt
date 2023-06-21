package com.example.talksy.presentation.main.chats

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talksy.presentation.graphs.navigation.GraphIconLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chats(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatsViewModelContainer: ChatsViewModelContainer
) {

    val navItems = listOf(
        GraphIconLabel.Chats,
        GraphIconLabel.Contacts,
        GraphIconLabel.Settings
    )

    val item = GraphIconLabel.Chats

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
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "chats")
        }
    }
}