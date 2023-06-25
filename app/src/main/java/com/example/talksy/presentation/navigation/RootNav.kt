package com.example.talksy.presentation.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.chatScreen.ChatScreen
import com.example.talksy.presentation.chatScreen.ChatScreenViewModel
import com.example.talksy.presentation.editProfile.EditProfile
import com.example.talksy.presentation.editProfile.EditProfileViewModel
import com.example.talksy.presentation.login.Login
import com.example.talksy.presentation.login.LoginViewModel
import com.example.talksy.presentation.main.chats.Chats
import com.example.talksy.presentation.main.chats.ChatsViewModel
import com.example.talksy.presentation.main.contacts.Contacts
import com.example.talksy.presentation.main.contacts.ContactsViewModel
import com.example.talksy.presentation.main.settings.Settings
import com.example.talksy.presentation.main.settings.SettingsViewModel
import com.example.talksy.presentation.onBoarding.OnBoarding
import com.example.talksy.presentation.onBoarding.OnBoardingViewModel
import com.example.talksy.presentation.register.Register
import com.example.talksy.presentation.register.RegisterViewModel

@Composable
fun RootNav(
    navController: NavHostController,
    rootViewModel: RootViewModel,
    onBoardingViewModel: OnBoardingViewModel,
    registerViewModel: RegisterViewModel,
    loginViewModel: LoginViewModel,
    chatsViewModel: ChatsViewModel,
    contactsViewModel: ContactsViewModel,
    settingsViewModel: SettingsViewModel,
    editProfileViewModel: EditProfileViewModel,
    chatScreenViewModel: ChatScreenViewModel
) {

    NavHost(
        navController = navController,
        route = Graph.Root.route,
        startDestination = if (rootViewModel.state.value.isUserLoggedIn) Graph.Main.route else Graph.Authentication.route
    ) {
        navigation(
            route = Graph.Authentication.route,
            startDestination = AuthScreen.OnBoarding.route
        ) {
            composable(route = AuthScreen.OnBoarding.route) {
                OnBoarding(
                    navController = navController,
                    state = onBoardingViewModel.state.value,
                    onEvent = onBoardingViewModel::onEvent,
                    events = onBoardingViewModel.events
                )
            }
            composable(route = AuthScreen.Register.route) {
                Register(
                    navController = navController,
                    state = registerViewModel.state.value,
                    onEvent = registerViewModel::onEvent,
                    events = registerViewModel.events
                )
            }
            composable(route = AuthScreen.Login.route) {
                Login(
                    navController = navController,
                    state = loginViewModel.state.value,
                    onEvent = loginViewModel::onEvent,
                    events = loginViewModel.events
                )
            }
        }
        navigation(
            route = Graph.Main.route,
            startDestination = GraphIconLabel.Chats.route
        ) {
            navigation(
                route = GraphIconLabel.Chats.route,
                startDestination = BottomNavScreen.Chats.route
            ) {
                composable(route = BottomNavScreen.Chats.route) {
                    Chats(
                        navController = navController,
                        state = chatsViewModel.state.value,
                        onEvent = chatsViewModel::onEvent,
                        events = chatsViewModel.events
                    )
                }
                composable(route = "${ChatsNav.ChatScreen.route}/{user2}") { backStackEntry ->
                    ChatScreen(
                        navController = navController,
                        state = chatScreenViewModel.state.value,
                        onEvent = chatScreenViewModel::onEvent,
                        events = chatScreenViewModel.events,
                        user2 = backStackEntry.arguments?.getString("user2")
                    )
                }
            }
            navigation(
                route = GraphIconLabel.Contacts.route,
                startDestination = BottomNavScreen.Contacts.route
            ) {
                composable(route = BottomNavScreen.Contacts.route) {
                    Contacts(
                        navController = navController,
                        state = contactsViewModel.state.value,
                        onEvent = contactsViewModel::onEvent,
                        events = contactsViewModel.events
                    )
                }
            }
            navigation(
                route = GraphIconLabel.Settings.route,
                startDestination = BottomNavScreen.Settings.route
            ) {
                composable(route = SettingsNav.EditProfile.route) {
                    EditProfile(
                        navController = navController,
                        state = editProfileViewModel.state.value,
                        onEvent = editProfileViewModel::onEvent,
                        events = editProfileViewModel.events
                    )
                }
                composable(route = BottomNavScreen.Settings.route) {
                    Settings(
                        navController = navController,
                        state = settingsViewModel.state.value,
                        onEvent = settingsViewModel::onEvent,
                        events = settingsViewModel.events
                    )
                }
            }
        }
    }
}

sealed class Graph(val route: String) {
    object Root : Graph("root")
    object Authentication : Graph("auth")
    object Main : Graph("main")
}

sealed class AuthScreen(val route: String) {
    object OnBoarding : AuthScreen(route = "on_boarding")
    object Login : AuthScreen(route = "login")
    object Register : AuthScreen(route = "register")
}

// main objects -

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Chats : BottomNavScreen("chats", "Chats", Icons.Default.Chat)
    object Contacts : BottomNavScreen("contacts", "Contacts", Icons.Default.Contacts)
    object Settings : BottomNavScreen("settings", "Settings", Icons.Default.Settings)
}

// graphs of each bottom nav screen
sealed class GraphIconLabel(val route: String, val label: String, val icon: ImageVector) {
    object Settings : GraphIconLabel("settings_graph", "Settings", Icons.Default.Settings)
    object Contacts : GraphIconLabel("contacts_graph", "Contacts", Icons.Default.Contacts)
    object Chats : GraphIconLabel("chats_graph", "Chats", Icons.Default.Chat)
}

// objects of bottom nav screens graphs
sealed class SettingsNav(val route: String) {
    object EditProfile : SettingsNav("edit_profile")
}

sealed class ChatsNav(val route: String) {
    object ChatScreen : ChatsNav("chat_screen")
}