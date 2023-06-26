package com.example.talksy.presentation.main.settings

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditAttributes
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.ReadMore
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.talksy.TalksyApp.Companion.TAG
import com.example.talksy.presentation.navigation.AuthScreen
import com.example.talksy.presentation.navigation.GraphIconLabel
import com.example.talksy.presentation.navigation.SettingsNav
import com.example.talksy.ui.theme.TalksyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: SettingsStates,
    onEvent: (SettingsEvent) -> Unit,
    events: SharedFlow<SettingsEvent>
) {

    val navItems = listOf(
        GraphIconLabel.Chats,
        GraphIconLabel.Contacts,
        GraphIconLabel.Settings
    )
    val item = GraphIconLabel.Settings

    //Handling events
    LaunchedEffect(key1 = true) {
        events.collectLatest { event ->
            when (event) {
                // TODO: temporary solution
                SettingsEvent.SignOut -> {
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(navController.backQueue.first().id) {
                            inclusive = true
                        }
                    }
                }

                SettingsEvent.GoToEditProfile -> {
                    navController.navigate(SettingsNav.EditProfile.route)
                }
            }
        }
    }

    Scaffold(bottomBar = {
        NavigationBar() {
            navItems.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "navigation item icon"
                        )
                    },
                    label = { Text(text = screen.label) },
                    selected = screen.route == item.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    })
            }
        }
    },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = item.label
                )
            })
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.fillMaxWidth(0.9f),
            ) {
                Spacer(modifier = modifier.height(20.dp))
                Log.d(TAG, "Settings: ${state.profilePicture}")
                Log.d(TAG, "Settings: ${state.email}")
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                ) {
                    Row(modifier = modifier.fillMaxSize()) {
                        Box(
                            modifier = modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        ) {
                            Image(
                                modifier = modifier
                                    .fillMaxSize(),
                                imageVector = Icons.Default.AccountCircle,
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                                contentDescription = "profile picture empty"
                            )
                            AsyncImage(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    ),
                                model = state.profilePicture,
                                contentDescription = "profile picture"
                            )
                        }
                        Column(
                            modifier = modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = state.username, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = state.email,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = modifier.height(25.dp))

                CustomTextButton(
                    leadingIcon = Icons.Outlined.EditAttributes,
                    text = "Edit Profile",
                    onClick = { onEvent(SettingsEvent.GoToEditProfile) }
                )
                CustomTextButton(
                    leadingIcon = Icons.Outlined.Block,
                    text = "Blocked Users"
                )
                CustomTextButton(
                    leadingIcon = Icons.Outlined.Delete,
                    text = "Delete Account"
                )
                CustomTextButton(
                    leadingIcon = Icons.Outlined.PrivacyTip,
                    text = "Privacy Policy"
                )
                CustomTextButton(
                    leadingIcon = Icons.Outlined.ReadMore,
                    text = "Terms & Conditions"
                )
                CustomTextButton(
                    leadingIcon = Icons.Outlined.Logout,
                    text = "Logout",
                    onClick = { onEvent(SettingsEvent.SignOut) }
                )
            }
        }
    }
}

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        onClick = { onClick() },
    ) {
        ConstraintLayout(modifier = modifier.fillMaxWidth()) {
            val (startIcon, centerText, endIcon) = createRefs()
            Icon(
                modifier = modifier.constrainAs(startIcon) {
                    centerVerticallyTo(parent)
                    start.linkTo(parent.start)
                },
                imageVector = leadingIcon,
                contentDescription = "leading icon",
            )
            Text(
                modifier = modifier
                    .padding(start = 8.dp)
                    .constrainAs(centerText) {
                        centerVerticallyTo(parent)
                        start.linkTo(startIcon.end)
                    },
                text = text
            )
            Icon(
                modifier = modifier.constrainAs(endIcon) {
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end)
                },
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "forward arrow"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPrev() {
    TalksyTheme(darkTheme = true) {
        Settings(
            navController = rememberNavController(),
            state = SettingsStates(username = "Ron189", "ronron18923@gmail.com"),
            onEvent = {},
            events = MutableSharedFlow<SettingsEvent>().asSharedFlow()
        )
    }
}