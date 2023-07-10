package com.example.talksy.presentation.chatScreen

import android.app.Activity
import android.net.Uri
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.data.dataModels.MessageView
import com.example.talksy.presentation.reusable.composables.EmojiTable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
    var showEmojiPicker by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current // to control the keyboard

    val inputFieldSource = remember {
        MutableInteractionSource()
    }

    if (inputFieldSource.collectIsPressedAsState().value) showEmojiPicker = false

    val cameraPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) onEvent(ChatScreenEvent.ClipImagePicked(uri))
        }

    if (showImagePicker) {
        showImagePicker = false
        cameraPickerLauncher.launch("image/*")
    }

    BackHandler(
        enabled = showEmojiPicker // if the emoji picker isn't shown, behave as usual.
    ) {
        showEmojiPicker = false
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
                ChatScreenEvent.EmojisClicked -> {
                    if (showEmojiPicker) {
                        showEmojiPicker = false
                        keyboardController?.show()
                    } else {
                        keyboardController?.hide()
                        showEmojiPicker = true
                    }
                }

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
                Column(modifier = modifier.fillMaxSize()) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
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
                    Row(
                        modifier = modifier
                            .padding(bottom = 10.dp, top = 8.dp)
                            .fillMaxWidth()
                            .padding(
                                horizontal = LocalConfiguration.current.screenWidthDp.times(
                                    0.03
                                ).dp
                            )
                            .height(50.dp),
//                            .align(Alignment.BottomCenter),
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
                                    onClick = { onEvent(ChatScreenEvent.EmojisClicked) },
                                    modifier = modifier.fillMaxHeight(0.5f)
                                ) {
                                    Icon(
                                        modifier = modifier
                                            .aspectRatio(1f),
                                        imageVector = if (showEmojiPicker) Icons.Default.Keyboard else Icons.Default.EmojiEmotions,
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
                                    interactionSource = inputFieldSource,
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
                    if (showEmojiPicker) {
                        EmojiTable() { onEvent(ChatScreenEvent.EmojiClicked(it)) }
                    }
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
                .widthIn(max = 280.dp)
                .onGloballyPositioned {
                    cardWidth = with(localDensity) { it.size.width.toDp() }
                },
            shape = messageShape(messageItem),
            colors = CardDefaults.cardColors(
                containerColor = messageColor(messageItem)
            ),
        ) {
            if (messageItem.image == Uri.EMPTY) {
                if (cardWidth < 280.dp) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = messageItem.message,
                            color = onMessageBgColor(messageItem),
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
                            color = onMessageBgColor(messageItem),
                        )
                        Text(
                            modifier = Modifier.padding(bottom = 3.dp, end = 9.dp),
                            text = messageItem.timestamp,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                var imageLoadingState by remember { mutableStateOf(true) }
                Box {
                    if (imageLoadingState) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = onMessageBgColor(messageItem)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        AsyncImage(
                            model = messageItem.image,
                            filterQuality = FilterQuality.None,
                            contentDescription = "message image",
                            modifier = Modifier
                                .heightIn(max = 450.dp)
                                .clip(messageShape(messageItem))
                                .border(
                                    width = 4.dp,
                                    color = messageColor(messageItem),
                                    shape = messageShape(messageItem)
                                ),
                            onSuccess = { imageLoadingState = false }
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
}

@Composable
fun messageColor(message: MessageView): Color {
    return when {
        message.isItMe -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondary
    }
}

@Composable
fun onMessageBgColor(message: MessageView): Color {
    return when {
        message.isItMe -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSecondary
    }
}

fun messageShape(message: MessageView): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        message.isItMe -> roundedCorners.copy(bottomStart = CornerSize(0))
        else -> roundedCorners.copy(bottomEnd = CornerSize(0))
    }
}

@Composable
fun EmojiPicker() {
    AndroidView(
        factory = { context ->
            EmojiPickerView(context)
        },
        modifier = Modifier.fillMaxWidth(),
    )
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