package com.example.talksy.presentation.main

sealed class MainEvent {
    object NoUserFound : MainEvent()

    data class NavItemSelected(val value: Int) : MainEvent()
}

data class MainStates(
    var selectedNavItem: Int = 0
)