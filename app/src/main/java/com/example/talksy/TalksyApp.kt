package com.example.talksy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TalksyApp: Application(){
    companion object {
        //tag for logs
        const val TAG = "qwe"

        //variables for ChatFrame's navigation composes
        const val STATE = "state"
        const val ONEVENT = "onEvent"
        const val EVENTS = "events"
    }
}