package com.example.talksy.presentation.startCompose

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.talksy.data.user.UserRepository
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.example.talksy.presentation.destinations.ChatFrameDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun StartCompose(
    navigator: DestinationsNavigator?,
    viewModel: StartComposeViewModel
) {
    // TODO: temporary solution.
    val user = viewModel.getUser()
    if (user == null){
        navigator?.navigate(OnBoardingDestination)
    }
    else{
        navigator?.navigate(ChatFrameDestination)
    }
}