package com.example.talksy.presentation.login

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.navigation.AuthScreen
import com.example.talksy.presentation.navigation.Graph
import com.example.talksy.presentation.reusable.composables.AutoScalingText
import com.example.talksy.presentation.reusable.composables.ProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
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

    var isShowDialog by remember { mutableStateOf(false) }

    if (isShowDialog) {
        Dialog(
            onDismissRequest = { isShowDialog = false },
            DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Card {
                Column(
                    modifier = modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Email:",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = modifier
                            .padding(bottom = 20.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        modifier = modifier.padding(bottom = 20.dp),
                        value = state.emailInput,
                        onValueChange = { onEvent(LoginEvent.EmailEntered(it)) },
                        label = { Text(text = "" + "Email")},
                    )
                    Button(
                        enabled = state.emailInput.isNotEmpty(),
                        onClick = {
                            onEvent(LoginEvent.ForgotPasswordDialogClicked)
                            CoroutineScope(Dispatchers.IO).launch {
                                snackbarHostState.showSnackbar("Link to reset your password has been sent to your Email.")
                            }
                            isShowDialog = false
                        }) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    }

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is LoginEvent.GoToRegisterClicked -> navController.navigate(AuthScreen.Register.route) {
                    popUpTo(AuthScreen.Login.route) {
                        inclusive = true
                    }
                }

                is LoginEvent.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(event.message) }
                is LoginEvent.GoBackClicked -> navController.popBackStack()
                is LoginEvent.GoToApp -> {
                    navController.navigate(Graph.Main.route) {
                        popUpTo(navController.backQueue.first().destination.id) {
                            inclusive = true
                        }
                    }
                }

                is LoginEvent.ForgotPasswordClicked -> isShowDialog = true

                else -> {} //not all events require implementation here.
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(text = "Login")
                        Spacer(modifier = modifier.height(4.dp))
                        Text(
                            text = "Enter your email and password to continue.",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
                        )
                    }
                }
            )
        }) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.showProgressDialog) {
                ProgressDialog()
            }
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
                        label = { Text("Enter password") },
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
                    Spacer(modifier = modifier.fillMaxHeight(0.10f))
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Forgot password?")
                            TextButton(
                                modifier = modifier.defaultMinSize(minHeight = 1.dp),
                                onClick = { onEvent(LoginEvent.ForgotPasswordClicked) }) {
                                Text(
                                    text = "Reset"
                                )
                            }
                        }
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Don't have an account?")
                            TextButton(
                                onClick = { navController.navigate(AuthScreen.Register.route) }) {
                                Text(
                                    text = "Register"
                                )
                            }
                        }
                    }
                    Spacer(modifier = modifier.fillMaxHeight(0.06f))
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPrev() {
    Login(
        navController = rememberNavController(),
        state = LoginStates("", "", false, true),
        onEvent = {},
        events = MutableSharedFlow<LoginEvent>().asSharedFlow()
    )
}