package com.example.talksy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talksy.TalksyApp.Companion.EVENTS
import com.example.talksy.TalksyApp.Companion.ONEVENT
import com.example.talksy.TalksyApp.Companion.STATE
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.chatFrame.ChatFrame
import com.example.talksy.presentation.login.Login
import com.example.talksy.presentation.NavGraphs
import com.example.talksy.presentation.chatFrame.ChatFrameViewModel
import com.example.talksy.presentation.chatFrame.chats.ChatsViewModel
import com.example.talksy.presentation.chatFrame.contacts.ContactsEvent
import com.example.talksy.presentation.chatFrame.contacts.ContactsViewModel
import com.example.talksy.presentation.chatFrame.settings.SettingsEvent
import com.example.talksy.presentation.chatFrame.settings.SettingsViewModel
import com.example.talksy.presentation.destinations.ChatFrameDestination
import com.example.talksy.presentation.onBoarding.OnBoarding
import com.example.talksy.presentation.register.Register
import com.example.talksy.presentation.destinations.LoginDestination
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.example.talksy.presentation.destinations.RegisterDestination
import com.example.talksy.presentation.destinations.StartComposeDestination
import com.example.talksy.presentation.login.LoginViewModel
import com.example.talksy.presentation.onBoarding.OnBoardingViewModel
import com.example.talksy.presentation.register.RegisterEvent
import com.example.talksy.presentation.register.RegisterViewModel
import com.example.talksy.presentation.startCompose.StartCompose
import com.example.talksy.presentation.startCompose.StartComposeViewModel
import com.example.talksy.ui.theme.TalksyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint

//TODO at the end of the project, make all of the compose functions parameters, non-nullable again.

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var startComposeViewModel: StartComposeViewModel
        lateinit var onBoardingViewModel: OnBoardingViewModel
        lateinit var registerViewModel: RegisterViewModel
        lateinit var loginViewModel: LoginViewModel
        lateinit var chatFrameViewModel: ChatFrameViewModel
        lateinit var settingsViewModel: SettingsViewModel
        lateinit var chatsViewModel: ChatsViewModel
        lateinit var contactsViewModel: ContactsViewModel

        super.onCreate(savedInstanceState)
        setContent {
            TalksyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    startComposeViewModel = hiltViewModel()
                    onBoardingViewModel = hiltViewModel()
                    registerViewModel = hiltViewModel()
                    loginViewModel = hiltViewModel()
                    chatFrameViewModel = hiltViewModel()
                    settingsViewModel = hiltViewModel()
                    chatsViewModel = hiltViewModel()
                    contactsViewModel = hiltViewModel()

                    val chatsViewModelMap = mapOf(
                        STATE to chatsViewModel.state.value,
                        ONEVENT to chatsViewModel::onEvent,
                        EVENTS to chatsViewModel.events
                    )
                    val contactsViewModelMap = mapOf(
                        STATE to contactsViewModel.state.value,
                        ONEVENT to contactsViewModel::onEvent,
                        EVENTS to contactsViewModel.events
                    )
                    val settingsViewModelMap = mapOf(
                        STATE to settingsViewModel.state.value,
                        ONEVENT to settingsViewModel::onEvent,
                        EVENTS to settingsViewModel.events
                    )

                    //forcing the layout direction of the app to always be ltr
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        DestinationsNavHost(navGraph = NavGraphs.root) {
                            composable(StartComposeDestination) {
                                StartCompose(
                                    navigator = destinationsNavigator,
                                    viewModel = startComposeViewModel
                                )
                            }
                            composable(OnBoardingDestination) {
                                OnBoarding(
                                    navigator = destinationsNavigator,
                                    state = onBoardingViewModel.state.value,
                                    onEvent = onBoardingViewModel::onEvent,
                                    events = onBoardingViewModel.events
                                )
                            }
                            composable(RegisterDestination) {
                                Register(
                                    navigator = destinationsNavigator,
                                    state = registerViewModel.state.value,
                                    onEvent = registerViewModel::onEvent,
                                    events = registerViewModel.events
                                )
                            }
                            composable(LoginDestination) {
                                Login(
                                    navigator = destinationsNavigator,
                                    state = loginViewModel.state.value,
                                    onEvent = loginViewModel::onEvent,
                                    events = loginViewModel.events
                                )
                            }
                            composable(ChatFrameDestination) {
                                ChatFrame(
                                    navigator = destinationsNavigator,
                                    state = chatFrameViewModel.state.value,
                                    onEvent = chatFrameViewModel::onEvent,
                                    events = chatFrameViewModel.events,
                                    chatsViewModelMap = chatsViewModelMap,
                                    contactsViewModelMap = contactsViewModelMap,
                                    settingsViewModelMap = settingsViewModelMap
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
