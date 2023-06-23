package com.example.talksy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.talksy.presentation.main.MainViewModel
import com.example.talksy.presentation.main.chats.ChatsViewModel
import com.example.talksy.presentation.main.contacts.ContactsViewModel
import com.example.talksy.presentation.main.settings.SettingsViewModel
import com.example.talksy.presentation.editProfile.EditProfileViewModel
import com.example.talksy.presentation.login.LoginViewModel
import com.example.talksy.presentation.graphs.navigation.RootNav
import com.example.talksy.presentation.graphs.navigation.RootViewModel
import com.example.talksy.presentation.onBoarding.OnBoardingViewModel
import com.example.talksy.presentation.register.RegisterViewModel
import com.example.talksy.ui.theme.TalksyTheme
import dagger.hilt.android.AndroidEntryPoint

//TODO at the end of the project, make all of the compose functions parameters, non-nullable again.

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var rootViewModel: RootViewModel
        lateinit var onBoardingViewModel: OnBoardingViewModel
        lateinit var registerViewModel: RegisterViewModel
        lateinit var loginViewModel: LoginViewModel
        lateinit var mainViewModel: MainViewModel
        lateinit var chatsViewModel: ChatsViewModel
        lateinit var contactsViewModel: ContactsViewModel
        lateinit var settingsViewModel: SettingsViewModel
        lateinit var editProfileViewModel: EditProfileViewModel

        super.onCreate(savedInstanceState)
        setContent {
            TalksyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    rootViewModel = hiltViewModel()

                    onBoardingViewModel = hiltViewModel()
                    registerViewModel = hiltViewModel()
                    loginViewModel = hiltViewModel()
                    mainViewModel = hiltViewModel()

                    chatsViewModel = hiltViewModel()
                    contactsViewModel = hiltViewModel()
                    settingsViewModel = hiltViewModel()
                    editProfileViewModel = hiltViewModel()

                    //forcing the layout direction of the app to always be ltr
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        RootNav(
                            navController = rememberNavController(),
                            rootViewModel = rootViewModel,
                            onBoardingViewModel = onBoardingViewModel,
                            registerViewModel = registerViewModel,
                            loginViewModel = loginViewModel,
                            mainViewModel = mainViewModel,

                            chatsViewModel = chatsViewModel,
                            contactsViewModel = contactsViewModel,
                            settingsViewModel = settingsViewModel,
                            editProfileViewModel = editProfileViewModel,
                        )
                    }
                }
            }
        }
    }
}
