package com.example.talksy.presentation.onBoarding

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talksy.presentation.register.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(

) : ViewModel() {
    private val _state = mutableStateOf(OnBoardingStates())
    val state: State<OnBoardingStates> = _state

    private val _events = MutableSharedFlow<OnBoardingEvent>()
    val events = _events.asSharedFlow()

    private val texts: Queue<String> = LinkedList(
        listOf(
            "Welcome to Talksy! chat and create memorable moments together.",
            "Discover new conversations. Dive into the world of Talksy!",
            "Talksy, your joyful companion for delightful chats."
        )
    )

    init {
        if (!texts.isEmpty()){
            _state.value = _state.value.copy(textState = texts.remove())
        }
    }

    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            OnBoardingEvent.NextClicked -> {
                if (!texts.isEmpty()){
                    _state.value = _state.value.copy(textState = texts.remove())
                }
                else onEvent(OnBoardingEvent.Finished)
            }

            OnBoardingEvent.SkipClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        OnBoardingEvent.SkipClicked
                    )
                }
            }

            OnBoardingEvent.Finished -> {
                viewModelScope.launch {
                    _events.emit(
                        OnBoardingEvent.Finished
                    )
                }
            }
        }
    }
}