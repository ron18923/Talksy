package com.example.talksy.presentation.startCompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.talksy.presentation.graphs.navigation.AuthScreen

@Composable
fun StartCompose(
    navController: NavController,
    viewModel: NavigationViewModel
) {
    // TODO: temporary solution.
    viewModel.onEvent(NavigationEvent.CheckSignedIn)
    if (viewModel.state.value.isSignedIn){
//        navController.navigate(Screen.ChatFrame.route)
    }
    else{
        navController.navigate(AuthScreen.OnBoarding.route)
   }
}