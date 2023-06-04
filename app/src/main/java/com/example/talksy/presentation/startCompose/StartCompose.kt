package com.example.talksy.presentation.startCompose

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.destinations.OnBoardingDestination
import com.example.talksy.presentation.destinations.ChatFrameDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun StartCompose(
    navigator: DestinationsNavigator?,
    viewModel: NavigationViewModel
) {
    // TODO: temporary solution.
    viewModel.onEvent(NavigationEvent.CheckSignedIn)
    Log.d(TAG, "StartCompose: ${viewModel.state.value.isSignedIn}")
    if (viewModel.state.value.isSignedIn){
        navigator?.navigate(ChatFrameDestination)
    }
    else{
        navigator?.navigate(OnBoardingDestination)
   }
}