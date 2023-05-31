package com.example.talksy.presentation.register

import android.app.Application
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talksy.R
import com.example.talksy.UserViewModel
import com.example.talksy.presentation.destinations.LoginDestination
import com.example.talksy.presentation.reusableComposables.AutoScalingText
import com.example.talksy.data.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
fun Register(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    state: RegisterStates,
    onEvent: (RegisterEvent) -> Unit,
    events: SharedFlow<RegisterEvent>
) {
    val passwordFieldIcon =
        if (state.isPasswordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
    val passwordFieldVisualTransformation =
        if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val configuration = LocalConfiguration.current
    val screenHeight =
        configuration.screenHeightDp.dp //to calculate elements height with reference to screen height

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //Handling events
    LaunchedEffect(key1 = true) {
        onEvent(RegisterEvent.GoToLoginClicked)

        events.collectLatest { event ->
            when (event) {
                is RegisterEvent.GoToLoginClicked -> {
                    navigator?.navigate(LoginDestination)
                }
                is RegisterEvent.ShowMessage -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(title = {
                Column {
                    Text(text = "Register")
                    Spacer(modifier = modifier.height(4.dp))
                    Text(
                        text = "Fill up your details to register.",
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
                    )
                }
            },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator?.popBackStack()
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
                        value = state.nameInput,
                        label = { Text("Enter your name") },
                        placeholder = { Text("Name") },
                        onValueChange = { onEvent(RegisterEvent.NameEntered(it)) })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = state.emailInput,
                        label = { Text("Enter email") },
                        placeholder = { Text("Email") },
                        onValueChange = { onEvent(RegisterEvent.EmailEntered(it)) })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = state.passwordInput,
                        label = { Text("Enter your password") },
                        placeholder = { Text("Password") },
                        onValueChange = { onEvent(RegisterEvent.PasswordEntered(it)) },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(RegisterEvent.PasswordVisibilityClicked) }) {
                                Icon(
                                    painter = painterResource(id = passwordFieldIcon),
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
                        onClick = {
                            onEvent(RegisterEvent.RegisterClicked)
                        }) {
                        AutoScalingText(
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            text = "Register"
                        )
                    }
                    Spacer(modifier = modifier.fillMaxHeight(0.05f))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Already have an account?")
                        TextButton(
                            onClick = { onEvent(RegisterEvent.GoToLoginClicked) }) {
                            Text(
                                text = "Login"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPrev() {
    Register(
        navigator = null,
        state = RegisterStates("", "", "", false, false),
        onEvent = {},
        events = MutableSharedFlow<RegisterEvent>().asSharedFlow()
    )
}