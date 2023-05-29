package com.example.talksy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.talksy.compose.ChatPage
import com.example.talksy.compose.Login
import com.example.talksy.compose.NavGraphs
import com.example.talksy.compose.OnBoarding
import com.example.talksy.compose.Register
import com.example.talksy.compose.destinations.ChatPageDestination
import com.example.talksy.compose.destinations.LoginDestination
import com.example.talksy.compose.destinations.OnBoardingDestination
import com.example.talksy.compose.destinations.RegisterDestination
import com.example.talksy.ui.theme.TalksyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint

//TODO at the end of the project, make all of the compose functions parameters, non-nullable again.

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var userViewModel: UserViewModel
        lateinit var chatViewModel: ChatViewModel

        super.onCreate(savedInstanceState)
        setContent {
            TalksyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    userViewModel = hiltViewModel()
                    chatViewModel = hiltViewModel()

                    //forcing the layout direction of the app to always be ltr
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        DestinationsNavHost(navGraph = NavGraphs.root) {
                            composable(OnBoardingDestination) {
                                OnBoarding(navigator = destinationsNavigator)
                            }
                            composable(RegisterDestination) {
                                Register(
                                    navigator = destinationsNavigator,
                                    userViewModel = userViewModel
                                )
                            }
                            composable(LoginDestination) {
                                Login(
                                    navigator = destinationsNavigator,
                                    userViewModel = userViewModel
                                )
                            }
                            composable(ChatPageDestination) {
                                ChatPage(
                                    navigator = destinationsNavigator,
                                    userViewModel = userViewModel,
                                    chatViewModel = chatViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
