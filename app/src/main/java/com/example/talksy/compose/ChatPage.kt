package com.example.talksy.compose

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.talksy.UserViewModel
import com.example.talksy.compose.destinations.OnBoardingDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    userViewModel: UserViewModel
) {
    if(userViewModel.isSignedIn()) navigator?.navigate(OnBoardingDestination)

}