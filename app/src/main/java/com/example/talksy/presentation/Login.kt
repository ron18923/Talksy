package com.example.talksy.presentation

import android.app.Application
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.talksy.presentation.destinations.ChatPageDestination
import com.example.talksy.presentation.destinations.RegisterDestination
import com.example.talksy.presentation.reusableComposables.AutoScalingText
import com.example.talksy.data.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    userViewModel: UserViewModel
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordFieldIcon =
        if (isPasswordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
    val passwordFieldVisualTransformation =
        if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val localContext = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    Scaffold(
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
                        value = userViewModel.emailInput.value,
                        label = { Text("Enter email") },
                        placeholder = { Text("Email") },
                        onValueChange = { userViewModel.emailInput.value = it })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = userViewModel.passwordInput.value,
                        label = { Text("Enter your password") },
                        placeholder = { Text("Password") },
                        onValueChange = { userViewModel.passwordInput.value = it },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
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
                            val isValid = userViewModel.checkIfFieldsValid(
                                email = userViewModel.emailInput.value,
                                password = userViewModel.passwordInput.value
                            ) { errorMessage ->
                                Toast.makeText(localContext, errorMessage, Toast.LENGTH_LONG).show()
                            }
                            if (isValid) {
                                val isSuccess = userViewModel.signInUser()
                                isSuccess.observe(lifeCycleOwner) {
                                    if(it) navigator?.navigate(ChatPageDestination)
                                }
                            }
                        }
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
        userViewModel = UserViewModel(
            UserRepository(
                Firebase.auth,
                Application()
            )
        )
    )
}