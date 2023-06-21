package com.example.talksy.presentation.register

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talksy.presentation.graphs.navigation.AuthScreen
import com.example.talksy.presentation.reusableComposables.AutoScalingText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: RegisterStates,
    onEvent: (RegisterEvent) -> Unit,
    events: SharedFlow<RegisterEvent>
) {
    val passwordFieldIcon =
        if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    val passwordFieldVisualTransformation =
        if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val configuration = LocalConfiguration.current
    val screenHeight =
        configuration.screenHeightDp.dp //to calculate elements height with reference to screen height

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is RegisterEvent.GoToLoginClicked -> navController.navigate(AuthScreen.Login.route){
                    popUpTo(AuthScreen.Register.route){
                        inclusive = true
                    }
                }
                is RegisterEvent.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(event.message) }
                is RegisterEvent.GoBackClicked -> navController.popBackStack()
//                is RegisterEvent.GoToApp -> navController.navigate(Screen.ChatFrame.route)
                else -> {} //not all events require implementation here.
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
//                navigationIcon = {
//                    IconButton(onClick = {
//                        onEvent(RegisterEvent.GoBackClicked)
//                    }) {
//                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
//                    }
//                }
            )
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
                        value = state.usernameInput,
                        label = { Text("Enter Username") },
                        placeholder = { Text("Username") },
                        onValueChange = { onEvent(RegisterEvent.UsernameEntered(it)) })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.emailInput,
                        label = { Text("Enter email") },
                        placeholder = { Text("Email") },
                        onValueChange = { onEvent(RegisterEvent.EmailEntered(it)) })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.passwordInput,
                        label = { Text("Enter your password") },
                        placeholder = { Text("Password") },
                        onValueChange = { onEvent(RegisterEvent.PasswordEntered(it)) },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(RegisterEvent.PasswordVisibilityClicked) }) {
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
        navController = rememberNavController(),
        state = RegisterStates("", "", "", false),
        onEvent = {},
        events = MutableSharedFlow<RegisterEvent>().asSharedFlow()
    )
}