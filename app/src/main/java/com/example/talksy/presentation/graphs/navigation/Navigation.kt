package com.example.talksy.presentation.graphs.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talksy.presentation.chatFrame.ChatFrame
import com.example.talksy.presentation.chatFrame.ChatFrameViewModel
import com.example.talksy.presentation.chatFrame.chats.Chats
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModel
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModelContainer
import com.example.talksy.presentation.chatFrame.contacts.Contacts
import com.example.talksy.presentation.chatFrame.contacts.ContactsViewModel
import com.example.talksy.presentation.chatFrame.contacts.ContactsViewModelContainer
import com.example.talksy.presentation.chatFrame.settings.Settings
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModel
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModelContainer
import com.example.talksy.presentation.chatScreen.ChatScreen
import com.example.talksy.presentation.chatScreen.ChatScreenViewModel
import com.example.talksy.presentation.editProfile.EditProfile
import com.example.talksy.presentation.editProfile.EditProfileViewModel
import com.example.talksy.presentation.login.Login
import com.example.talksy.presentation.login.LoginViewModel
import com.example.talksy.presentation.onBoarding.OnBoarding
import com.example.talksy.presentation.onBoarding.OnBoardingViewModel
import com.example.talksy.presentation.register.Register
import com.example.talksy.presentation.register.RegisterViewModel
import com.example.talksy.presentation.startCompose.StartCompose

@Composable
fun Navigation() {

    val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
    val registerViewModel = hiltViewModel<RegisterViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val chatFrameViewModel = hiltViewModel<ChatFrameViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val chatsViewModel = hiltViewModel<ChatsViewModel>()
    val editProfileViewModel = hiltViewModel<EditProfileViewModel>()
    val contactsViewModel = hiltViewModel<ContactsViewModel>()
    val chatScreenViewModel = hiltViewModel<ChatScreenViewModel>()

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

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.StartCompose.route) {
        composable(route = Screen.StartCompose.route) {
            StartCompose(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = Screen.OnBoarding.route) {
            OnBoarding(
                navController = navController,
                state = onBoardingViewModel.state.value,
                onEvent = onBoardingViewModel::onEvent,
                events = onBoardingViewModel.events
            )
        }
        composable(route = Screen.Register.route) {
            Register(
                navController = navController,
                state = registerViewModel.state.value,
                onEvent = registerViewModel::onEvent,
                events = registerViewModel.events
            )
        }
        composable(route = Screen.Login.route) {
            Login(
                navController = navController,
                state = loginViewModel.state.value,
                onEvent = loginViewModel::onEvent,
                events = loginViewModel.events
            )
        }
        composable(route = Screen.ChatFrame.route) {
            ChatFrame(
                navController = navController,
                state = chatFrameViewModel.state.value,
                onEvent = chatFrameViewModel::onEvent,
                events = chatFrameViewModel.events,
                chatsViewModelContainer = chatsViewModelContainer,
                contactsViewModelContainer = contactsViewModelContainer,
                settingsViewModelContainer = settingsViewModelContainer
            )
        }
        composable(route = BottomNavScreen.Chats.route) {
            Chats(
                navController = navController,
                chatsViewModelContainer = chatsViewModelContainer
            )
        }
        composable(route = BottomNavScreen.Contacts.route) {
            Contacts(
                navController = navController,
                contactsViewModelContainer = contactsViewModelContainer
            )
        }
        composable(route = BottomNavScreen.Settings.route) {
            Settings(
                navController = navController,
                settingsViewModelContainer = settingsViewModelContainer
            )
        }
        composable(route = Screen.EditProfile.route) {
            EditProfile(
                navController = navController,
                state = editProfileViewModel.state.value,
                onEvent = editProfileViewModel::onEvent,
                events = editProfileViewModel.events
            )
        }
        composable(route = Screen.ChatScreen.route) {
            ChatScreen(
                navController = navController,
                state = chatScreenViewModel.state.value,
                onEvent = chatScreenViewModel::onEvent,
                events = chatScreenViewModel.events
            )
        }
    }
}

//DestinationsNavHost(navGraph = NavGraphs.root) {
//    composable(StartComposeDestination) {
//        StartCompose(
//            navigator = destinationsNavigator,
//            viewModel = navigationViewModel
//        )
//    }
//    composable(OnBoardingDestination) {
//        OnBoarding(
//            navigator = destinationsNavigator,
//            state = onBoardingViewModel.state.value,
//            onEvent = onBoardingViewModel::onEvent,
//            events = onBoardingViewModel.events
//        )
//    }
//    composable(RegisterDestination) {
//        Register(
//            navigator = destinationsNavigator,
//            state = registerViewModel.state.value,
//            onEvent = registerViewModel::onEvent,
//            events = registerViewModel.events
//        )
//    }
//    composable(LoginDestination) {
//        Login(
//            navigator = destinationsNavigator,
//            state = loginViewModel.state.value,
//            onEvent = loginViewModel::onEvent,
//            events = loginViewModel.events
//        )
//    }
//    composable(ChatFrameDestination) {
//        ChatFrame(
//            navigator = destinationsNavigator,
//            state = chatFrameViewModel.state.value,
//            onEvent = chatFrameViewModel::onEvent,
//            events = chatFrameViewModel.events,
//            chatsViewModelContainer = chatsViewModelContainer,
//            contactsViewModelContainer = contactsViewModelContainer,
//            settingsViewModelContainer = settingsViewModelContainer
//        )
//    }
//    composable(EditProfileDestination) {
//        EditProfile(
//            navigator = destinationsNavigator,
//            state = editProfileViewModel.state.value,
//            onEvent = editProfileViewModel::onEvent,
//            events = editProfileViewModel.events,
//        )
//    }
//
//    composable(ChatScreenDestination) {
//        ChatScreen(
//            navigator = destinationsNavigator,
//            state = chatScreenViewModel.state.value,
//            onEvent = chatScreenViewModel::onEvent,
//            events = chatScreenViewModel.events
//        )
//    }
//
//    composable(CameraCaptureDestination) {
//        CameraCapture(navigator = destinationsNavigator)
//    }