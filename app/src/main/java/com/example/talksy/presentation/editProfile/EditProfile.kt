package com.example.talksy.presentation.editProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talksy.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator?,
    state: EditProfileStates,
    onEvent: (EditProfileEvent) -> Unit,
    events: SharedFlow<EditProfileEvent>
) {

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is EditProfileEvent.GoBackClicked -> navigator?.popBackStack()
                else -> {} //not all events require implementation here.
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(EditProfileEvent.GoBackClicked)
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
                modifier = modifier.fillMaxWidth(0.88f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = modifier
                            .height(100.dp)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                        contentDescription = "profile picture"
                    )
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        TextButton(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { /*TODO*/ },
                        ) {
                            Text(
                                text = "Change profile",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    singleLine = true,
                    value = state.username,
                    label = { Text("Enter name") },
                    placeholder = { Text("Name") },
                    onValueChange = { onEvent(EditProfileEvent.UsernameChanged(it)) })
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    singleLine = true,
                    value = state.email,
                    label = { Text("Enter email") },
                    placeholder = { Text("Email") },
                    onValueChange = { onEvent(EditProfileEvent.EmailChanged(it)) })
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Change Password", style = MaterialTheme.typography.titleSmall)
                }
                Button(
                    modifier = modifier.height(60.dp),
                    onClick = {
                        onEvent(EditProfileEvent.UpdateProfileClicked)
                    }) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        text = "Update Profile"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EditProfilePrev() {
    EditProfile(
        navigator = null,
        state = EditProfileStates(""),
        onEvent = {},
        events = MutableSharedFlow<EditProfileEvent>().asSharedFlow()
    )
}