package com.example.talksy.presentation.editProfile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.camera.core.Preview as Prev
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.destinations.CameraCaptureDestination
import com.example.talksy.ui.theme.TalksyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    var isShowDialog by remember { mutableStateOf(false) }
    var isChangeProfile by remember { mutableStateOf(false) }

    val cameraPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            Log.d(TAG, "EditProfile: asd")
            if (uri != null) onEvent(EditProfileEvent.ImagePicked(uri))
        }

    if (isShowDialog) {
        ChangePasswordDialog { isConfirmed ->
            isShowDialog = false
            if (isConfirmed) onEvent(EditProfileEvent.ChangePasswordConfirmed)
        }
    }

    if (isChangeProfile) {
        isChangeProfile = false
        cameraPickerLauncher.launch("image/*")
    }

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                is EditProfileEvent.GoBackClicked -> navigator?.popBackStack()
                is EditProfileEvent.ChangePasswordClicked -> isShowDialog = true
                is EditProfileEvent.ChangePasswordConfirmed -> scope.launch {
                    snackbarHostState.showSnackbar(
                        "Reset link sent to Email."
                    )
                }

                is EditProfileEvent.ChangeProfileClicked -> isChangeProfile = true

                else -> {} //not all events require implementation here.
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                modifier = modifier
                    .fillMaxWidth(0.88f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = state.profileImage),
                            modifier = modifier.fillMaxSize(),
                            contentDescription = "profile picture",
                        )
                    }
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        TextButton(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { /*TODO*/ },
                        ) {
                            TextButton(
                                onClick = {
                                    onEvent(EditProfileEvent.ChangeProfileClicked)
                                }) {
                                Text(
                                    text = "Change profile",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
                Button(
                    modifier = modifier.height(60.dp),
                    onClick = {
                        onEvent(EditProfileEvent.ChangePasswordClicked)
                    }) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        text = "Change password"
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
        state = EditProfileStates(),
        onEvent = {},
        events = MutableSharedFlow<EditProfileEvent>().asSharedFlow()
    )
}

@Composable
fun ChangePasswordDialog(
    onDismissRequest: (Boolean) -> Unit,
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismissRequest(false) },
        title = { Text(text = "Reset Password") },
        text = { Text(text = "Link to reset your password will be sent to your Email.") },
        confirmButton = {
            TextButton(
                onClick = { onDismissRequest(true) },
            ) { Text(text = "Reset") }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest(false) }) {
                Text(text = "Dismiss")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    TalksyTheme() {
        ChangePasswordDialog {}
    }
}