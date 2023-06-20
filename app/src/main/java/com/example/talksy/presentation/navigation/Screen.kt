package com.example.talksy.presentation.navigation

sealed class Screen(val route: String) {
    object StartCompose : Screen("start_compose")
    object OnBoarding : Screen("on_boarding")
    object Register : Screen("register")
    object Login : Screen("login")
    object ChatFrame : Screen("chat_frame")
    object EditProfile : Screen("edit_profile")
    object ChatScreen : Screen("chat_screen")
}
