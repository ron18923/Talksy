package com.example.talksy.presentation.editProfile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.talksy.R
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.ui.theme.TalksyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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

    if (isShowDialog) {
        ChangePasswordDialog { isConfirmed ->
            isShowDialog = false
            if (isConfirmed) onEvent(EditProfileEvent.ChangePasswordConfirmed)
        }
    }

    if (isChangeProfile) {
        val context = LocalContext.current
        val permission = Manifest.permission.CAMERA
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
//                CameraView(
//                    outputDirectory = outputDirectory,
//                    executor = cameraExecutor,
//                    onImageCaptured = ::handleImageCapture,
//                    onError = { Log.e("kilo", "View error:", it) }
//                )
//                outputDirectory = getOutputDirectory()
//                cameraExecutor = Executors.newSingleThreadExecutor()
            } else {
                // TODO: Show dialog
            }
        }
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
//            CameraView(
//                outputDirectory = outputDirectory,
//                executor = cameraExecutor,
//                onImageCaptured = ::handleImageCapture,
//                onError = { Log.e("kilo", "View error:", it) }
//            )
//            outputDirectory = getOutputDirectory()
//            cameraExecutor = Executors.newSingleThreadExecutor()
        } else {
            // Request a permission
            SideEffect { launcher.launch(permission) }
        }
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
                    Image(
                        modifier = modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f),
                        imageVector = Icons.Default.AccountCircle,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                        contentDescription = "profile picture",
                    )
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
        state = EditProfileStates(""),
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

//private fun takePhoto(
//    filenameFormat: String,
//    imageCapture: ImageCapture,
//    outputDirectory: File,
//    executor: Executor,
//    onImageCaptured: (Uri) -> Unit,
//    onError: (ImageCaptureException) -> Unit
//) {
//
//    val photoFile = File(
//        outputDirectory,
//        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
//    )
//
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
//        override fun onError(exception: ImageCaptureException) {
//            Log.e("kilo", "Take photo error:", exception)
//            onError(exception)
//        }
//
//        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//            val savedUri = Uri.fromFile(photoFile)
//            onImageCaptured(savedUri)
//        }
//    })
//}
//
//private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
//    suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }
//
//@Composable
//fun CameraView(
//    outputDirectory: File,
//    executor: Executor,
//    onImageCaptured: (Uri) -> Unit,
//    onError: (ImageCaptureException) -> Unit
//) {
//    // 1
//    val lensFacing = CameraSelector.LENS_FACING_BACK
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val preview = Preview.Builder().build()
//    val previewView = remember { PreviewView(context) }
//    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
//    val cameraSelector = CameraSelector.Builder()
//        .requireLensFacing(lensFacing)
//        .build()
//
//    // 2
//    LaunchedEffect(lensFacing) {
//        val cameraProvider = context.getCameraProvider()
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(
//            lifecycleOwner,
//            cameraSelector,
//            preview,
//            imageCapture
//        )
//
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//    }
//
//    // 3
//    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
//        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
//
//        IconButton(
//            modifier = Modifier.padding(bottom = 20.dp),
//            onClick = {
//                Log.i("kilo", "ON CLICK")
//                takePhoto(
//                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
//                    imageCapture = imageCapture,
//                    outputDirectory = outputDirectory,
//                    executor = executor,
//                    onImageCaptured = onImageCaptured,
//                    onError = onError
//                )
//            },
//            content = {
//                Icon(
//                    imageVector = Icons.Sharp.Lens,
//                    contentDescription = "Take picture",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(100.dp)
//                        .padding(1.dp)
//                        .border(1.dp, Color.White, CircleShape)
//                )
//            }
//        )
//    }
//}
//
//private fun handleImageCapture(uri: Uri) {
//    Log.i("kilo", "Image captured: $uri")
//    shouldShowCamera.value = false
//}
//
//private fun getOutputDirectory(): File {
//    val mediaDir = externalMediaDirs.firstOrNull()?.let {
//        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
//    }
//
//    return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
//}