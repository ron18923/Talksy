package com.example.talksy.compose

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talksy.R
import com.example.talksy.compose.destinations.RegisterDestination
import com.example.talksy.compose.reusableComposables.AutoScalingText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun OnBoarding(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

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
                    navigator?.navigate(RegisterDestination())
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
                text = "Welcome to Talksy, a great friend to chat with you",
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
                onClick = { /*TODO*/ }) {
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

@Preview(showBackground = true)
@Composable
fun OnBoardingPrev() {
    OnBoarding(navigator = null)
}