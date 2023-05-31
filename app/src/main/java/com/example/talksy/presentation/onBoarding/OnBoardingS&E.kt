package com.example.talksy.presentation.onBoarding

sealed class OnBoardingEvent {
    object SkipClicked : OnBoardingEvent()
    object NextClicked : OnBoardingEvent()
    object Finished : OnBoardingEvent()
}

data class OnBoardingStates (
    var textState : String = ""
        )
