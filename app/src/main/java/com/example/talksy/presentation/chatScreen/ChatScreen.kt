package com.example.talksy.presentation.chatScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ChatScreenStates,
    onEvent: (ChatScreenEvent) -> Unit,
    events: SharedFlow<ChatScreenEvent>
) {

    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                ChatScreenEvent.GoBackClicked -> navController.popBackStack()
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    ) {
                        Image(
                            modifier = modifier
                                .fillMaxSize(),
                            imageVector = Icons.Default.AccountCircle,
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                            contentDescription = "profile picture empty"
                        )
                        Image(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(6.dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.onBackground,
                                    CircleShape
                                ),
                            painter = rememberAsyncImagePainter(model = state.otherProfile),
                            contentDescription = "profile picture"
                        )
                    }
                    Text(text = state.otherUsername)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(ChatScreenEvent.GoBackClicked)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                })
        }) { contentPadding ->
        contentPadding
    }
}