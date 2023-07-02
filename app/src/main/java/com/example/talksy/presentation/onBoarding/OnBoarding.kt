package com.example.talksy.presentation.onBoarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talksy.R
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.navigation.AuthScreen
import com.example.talksy.presentation.reusable.composables.AutoScalingText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OnBoarding(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: OnBoardingStates,
    onEvent: (OnBoardingEvent) -> Unit,
    events: SharedFlow<OnBoardingEvent>
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Log.d(TAG, "OnBoarding: ")
    LaunchedEffect(key1 = true) {
        Log.d(TAG, "OnBoarding: in launched effect")
        events.collectLatest { event ->
            when (event) {
                OnBoardingEvent.SkipClicked -> navigateNext(navController)
                OnBoardingEvent.Finished -> navigateNext(navController)
                else -> {}
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = screenWidth.times(0.06.toFloat()))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {

            Spacer(modifier = modifier.height(screenHeight.times(0.05.toFloat())))

            FilledTonalButton(
                //adding padding to the top of 5% of screen's height
                modifier = modifier
                    .align(Alignment.End),
                onClick = {
                    onEvent(OnBoardingEvent.SkipClicked)
                }) {
                Text(text = "Skip", fontSize = 14.sp)
            }

            Spacer(modifier = modifier.height(screenHeight.times(0.05.toFloat())))

            Image(
                modifier = modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.on_boarding_screen_illustration),
                contentDescription = "on-boarding screen illustration"
            )

            Spacer(modifier = modifier.height(screenHeight.times(0.05.toFloat())))

            Text(
                text = state.textState,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(modifier = modifier.fillMaxSize()) {
            Button(
                modifier = modifier.height(screenHeight.times(0.06.toFloat())),
                onClick = {
                    onEvent(OnBoardingEvent.NextClicked)
                }) {
                AutoScalingText(
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    text = "Next"
                )
            }
        }
    }
}

fun navigateNext(navController: NavController){
    navController.navigate(AuthScreen.Register.route){
        popUpTo(0)
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingPrev() {
    OnBoarding(
        navController = rememberNavController(),
        state = OnBoardingStates("Welcome to Talksy, a great friend to chat with you"),
        onEvent = {},
        events = MutableSharedFlow<OnBoardingEvent>().asSharedFlow()
    )
}