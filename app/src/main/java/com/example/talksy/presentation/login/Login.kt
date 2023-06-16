package com.example.talksy.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talksy.R
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.destinations.ChatFrameDestination
import com.example.talksy.presentation.destinations.RegisterDestination
import com.example.talksy.presentation.reusableComposables.AutoScalingText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    state: LoginStates,
    onEvent: (LoginEvent) -> Unit,
    events: SharedFlow<LoginEvent>
) {
    val passwordFieldIcon =
        if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    val passwordFieldVisualTransformation =
        if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is LoginEvent.GoToRegisterClicked -> navigator?.navigate(RegisterDestination)
                is LoginEvent.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(event.message) }
                is LoginEvent.GoBackClicked -> navigator?.popBackStack()
                is LoginEvent.GoToApp -> {
                    Log.d(TAG, "Login: GoToApp")
                    navigator?.navigate(ChatFrameDestination)
                }
                else -> {} //not all events require implementation here.
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(title = {
                Column {
                    Text(text = "Login")
                    Spacer(modifier = modifier.height(4.dp))
                    Text(
                        text = "Enter your email and password to continue.",
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
                    )
                }
            },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(LoginEvent.GoBackClicked)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                })
        }) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.88f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = modifier.weight(1.5f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.emailInput,
                        label = { Text("Enter email") },
                        placeholder = { Text("Email") },
                        onValueChange = { onEvent(LoginEvent.EmailEntered(it)) })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.passwordInput,
                        label = { Text("Enter your password") },
                        placeholder = { Text("Password") },
                        onValueChange = { onEvent(LoginEvent.PasswordEntered(it)) },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(LoginEvent.PasswordVisibilityClicked) }) {
                                Icon(
                                    imageVector = passwordFieldIcon,
                                    contentDescription = "visibility toggle"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = passwordFieldVisualTransformation
                    )
                }
                Column(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Button(
                        modifier = modifier.height(screenHeight.times(0.06.toFloat())),
                        onClick = { onEvent(LoginEvent.LoginClicked) }
                    ) {
                        AutoScalingText(
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            text = "Login"
                        )
                    }
                    Spacer(modifier = modifier.fillMaxHeight(0.05f))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Don't have an account?")
                        TextButton(
                            onClick = { navigator?.navigate(RegisterDestination) }) {
                            Text(
                                text = "Register"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPrev() {
    Login(
        navigator = null,
        state = LoginStates("", "", false),
        onEvent = {},
        events = MutableSharedFlow<LoginEvent>().asSharedFlow()
    )
}