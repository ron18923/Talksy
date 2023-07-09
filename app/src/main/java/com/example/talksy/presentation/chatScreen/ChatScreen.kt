package com.example.talksy.presentation.chatScreen

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Paint.Align
import android.net.Uri
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.R
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.data.dataModels.MessageView
import com.example.talksy.presentation.editProfile.EditProfileEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ChatScreenStates,
    onEvent: (ChatScreenEvent) -> Unit,
    events: SharedFlow<ChatScreenEvent>,
    user2: String?,
    activity: Activity = LocalContext.current as Activity //added as a parameter so preview works.+
) {
//    this code is for the keyboard to overlap the screen.
    activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    var showImagePicker by remember { mutableStateOf(false) }

    val cameraPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) onEvent(ChatScreenEvent.ClipImagePicked(uri))
        }

    if (showImagePicker) {
        showImagePicker = false
        cameraPickerLauncher.launch("image/*")
    }

    DisposableEffect(Unit) {
        onDispose {
            onEvent(ChatScreenEvent.Dispose)
        }
    }

    LaunchedEffect(key1 = true) {
        user2?.let {
            onEvent(ChatScreenEvent.SetUser2(user2))
        }

        events.collectLatest { event ->
            when (event) {
                ChatScreenEvent.GoBackClicked -> navController.popBackStack()
                ChatScreenEvent.ClipClicked -> showImagePicker = true
                else -> {}
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = modifier
                                .size(40.dp)
                                .aspectRatio(1f)
                        ) {
                            Icon(
                                modifier = modifier
                                    .fillMaxSize(),
                                imageVector = Icons.Default.AccountCircle,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "profile picture empty"
                            )
                            Image(
                                modifier = modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                painter = rememberAsyncImagePainter(model = state.otherProfile),
                                contentDescription = "profile picture",
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Text(
                            modifier = modifier.padding(start = 8.dp),
                            text = state.user2,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(ChatScreenEvent.GoBackClicked)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.showProgressBar) CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = modifier.height(4.dp))
                    val listState = rememberLazyListState()
                    LaunchedEffect(key1 = state.messages.size) {
                        listState.animateScrollToItem(state.messages.size)
                    }
                    LazyColumn(
                        state = listState,
                        modifier = modifier
                            .weight(1f)
                            .padding(
                                horizontal = LocalConfiguration.current.screenWidthDp.times(
                                    0.06
                                ).dp
                            ),
                    ) {
                        state.messages.forEach { message ->
                            item {
                                MessageCard(messageItem = message)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = modifier
                    .padding(bottom = 10.dp, top = 8.dp)
                    .fillMaxWidth()
                    .padding(
                        horizontal = LocalConfiguration.current.screenWidthDp.times(
                            0.03
                        ).dp
                    )
                    .height(50.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            horizontal = LocalConfiguration.current.screenWidthDp.times(
                                0.02
                            ).dp
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(40.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val spacingValue = 10.dp
                    Spacer(modifier = modifier.width(spacingValue))
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = modifier.fillMaxHeight(0.5f)
                        ) {
                            Icon(
                                modifier = modifier
                                    .aspectRatio(1f),
                                imageVector = Icons.Default.EmojiEmotions,
                                contentDescription = "Emojis",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = modifier.width(spacingValue))
                        BasicTextField(
                            modifier = modifier
                                .defaultMinSize(0.dp)
                                .weight(1f),
                            value = state.inputText,
                            onValueChange = { onEvent(ChatScreenEvent.InputChange(it)) },
                            maxLines = 3,
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = modifier.padding(start = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (state.inputText.isEmpty()) Text(
                                        text = "Message",
                                        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                    innerTextField()
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = modifier.width(spacingValue))

                        IconButton(
                            onClick = { onEvent(ChatScreenEvent.ClipClicked) },
                            modifier = modifier.fillMaxHeight(0.5f)
                        ) {
                            Icon(
                                modifier = modifier
                                    .aspectRatio(1f),
                                contentDescription = "clip",
                                imageVector = Icons.Default.AttachFile,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(modifier = modifier.width(spacingValue))
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = modifier.fillMaxHeight(0.5f)
                        ) {
                            Icon(
                                modifier = modifier
                                    .aspectRatio(1f), contentDescription = "clip",
                                imageVector = Icons.Default.CameraAlt,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = modifier.width(spacingValue))
                    }
                }
                IconButton(
                    modifier = modifier.aspectRatio(1f),
                    onClick = { onEvent(ChatScreenEvent.SendClicked) },
                    colors = IconButtonDefaults.filledIconButtonColors()
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "send icon")
                }
            }
        }
    }
}

object ActivityPreview : Activity()

@Composable
fun MessageCard(messageItem: MessageView) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = when {
            messageItem.isItMe -> Alignment.Start
            else -> Alignment.End
        },
    ) {
        var cardWidth by remember { mutableStateOf(0.dp) }
        val localDensity = LocalDensity.current
        Card(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .onGloballyPositioned {
                    cardWidth = with(localDensity) { it.size.width.toDp() }
                },
            shape = messageShape(messageItem),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    messageItem.isItMe -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                }
            ),
        ) {
            if (cardWidth < 300.dp) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = messageItem.message,
                        color = when {
                            messageItem.isItMe -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSecondary
                        },
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 3.dp, end = 8.dp),
                        text = messageItem.timestamp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = messageItem.message,
                        color = when {
                            messageItem.isItMe -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSecondary
                        },
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 3.dp, end = 9.dp),
                        text = messageItem.timestamp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


@Composable
fun messageShape(message: MessageView): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        message.isItMe -> roundedCorners.copy(bottomStart = CornerSize(0))
        else -> roundedCorners.copy(bottomEnd = CornerSize(0))
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPrev() {
    ChatScreen(
        navController = rememberNavController(),
        state = ChatScreenStates(
            messages = arrayListOf(
                MessageView("Hey, how are you? I am fine.", "16:08", true),
                MessageView("Good. How you doing?", "16:09", false),
                MessageView("Hey, how are you? I am fine.", "16:10", true),
                MessageView("Good. How you doing?", "16:11", false),
                MessageView("Hey, how are you? I am fine.", "16:11", true),
                MessageView("Good. How you doing?", "16:11", false),
                MessageView("Hey, how are you? I am fine.", "16:18", true),
                MessageView("Good. How you doing?", "16:32", false),
                MessageView("Hey, how are you? I am fine.", "16:44", true),
                MessageView(
                    "Good. How you doing? Good. How you doing? Good. How you doing? Good. How you doing? ",
                    "16:46",
                    false
                ),
            ),
            showProgressBar = false
        ),
        onEvent = {},
        events = MutableSharedFlow<ChatScreenEvent>().asSharedFlow(),
        user2 = "Ron189",
        activity = ActivityPreview
    )
}