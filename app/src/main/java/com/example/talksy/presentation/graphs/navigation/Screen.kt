package com.example.talksy.presentation.graphs.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object StartCompose : Screen("start_compose")
    object OnBoarding : Screen("on_boarding")
    object Register : Screen("register")
    object Login : Screen("login")
    object ChatFrame : Screen("chat_frame")
    object EditProfile : Screen("edit_profile")
    object ChatScreen : Screen("chat_screen")
}

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Chats : BottomNavScreen("chats", "Chats", Icons.Default.Chat)
    object Contacts : BottomNavScreen("contacts", "Contacts", Icons.Default.Contacts)
    object Settings : BottomNavScreen("settings", "Settings", Icons.Default.Settings)
}
