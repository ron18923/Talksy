package com.example.talksy.presentation.graphs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import com.example.talksy.presentation.main.chats.Chats
import com.example.talksy.presentation.main.chats.ChatsViewModel
import com.example.talksy.presentation.main.chats.ChatsViewModelContainer
import com.example.talksy.presentation.main.contacts.Contacts
import com.example.talksy.presentation.main.contacts.ContactsViewModel
import com.example.talksy.presentation.main.contacts.ContactsViewModelContainer
import com.example.talksy.presentation.main.settings.Settings
import com.example.talksy.presentation.main.settings.SettingsViewModel
import com.example.talksy.presentation.main.settings.SettingsViewModelContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.talksy.presentation.editProfile.EditProfile
import com.example.talksy.presentation.editProfile.EditProfileViewModel
import com.example.talksy.presentation.startCompose.StartCompose

@Composable
fun MainNav(navController: NavHostController) {

    val chatsViewModel = hiltViewModel<ChatsViewModel>()
    val contactsViewModel = hiltViewModel<ContactsViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val editProfileViewModel = hiltViewModel<EditProfileViewModel>()

    // TODO: implement it correctly later
    val chatsViewModelContainer = ChatsViewModelContainer(
        chatsViewModel.state.value,
        chatsViewModel::onEvent,
        chatsViewModel.events
    )

    val contactsViewModelContainer = ContactsViewModelContainer(
        contactsViewModel.state.value,
        contactsViewModel::onEvent,
        contactsViewModel.events
    )

    val settingsViewModelContainer = SettingsViewModelContainer(
        settingsViewModel.state.value,
        settingsViewModel::onEvent,
        settingsViewModel.events
    )

//    NavHost(
//        navController = navController,
//        route = Graph.MAIN,
//        startDestination = BottomNavScreen.Chats.route
//    ) {
//        composable(route = BottomNavScreen.Chats.route) {
//            Chats(
//                navController = navController,
//                chatsViewModelContainer = chatsViewModelContainer
//            )
//        }
//        composable(route = BottomNavScreen.Contacts.route) {
//            Contacts(
//                navController = navController,
//                contactsViewModelContainer = contactsViewModelContainer
//            )
//        }
//        composable(route = BottomNavScreen.Settings.route) {
//            Settings(
//                navController = navController,
//                settingsViewModelContainer = settingsViewModelContainer
//            )
//        }
//        settingsNav(navController, editProfileViewModel)
//    }
}

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Chats : BottomNavScreen("chats", "Chats", Icons.Default.Chat)
    object Contacts : BottomNavScreen("contacts", "Contacts", Icons.Default.Contacts)
    object Settings : BottomNavScreen("settings", "Settings", Icons.Default.Settings)
}

fun NavGraphBuilder.settingsNav(
    navController: NavHostController,
    editProfileViewModel: EditProfileViewModel
) {
//
//    navigation(
//        route = Graph.SETTINGS,
//        startDestination = SettingsNav.EditProfile.route
//    ) {
//        composable(route = SettingsNav.EditProfile.route) {
//            EditProfile(
//                navController = navController,
//                state = editProfileViewModel.state.value,
//                onEvent = editProfileViewModel::onEvent,
//                events = editProfileViewModel.events
//            )
//        }
//    }
}


sealed class SettingsNav(val route: String) {
    object EditProfile : SettingsNav("edit_profile")
}