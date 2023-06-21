package com.example.talksy.presentation.graphs.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.talksy.presentation.login.Login
import com.example.talksy.presentation.login.LoginViewModel
import com.example.talksy.presentation.onBoarding.OnBoarding
import com.example.talksy.presentation.onBoarding.OnBoardingViewModel
import com.example.talksy.presentation.register.Register
import com.example.talksy.presentation.register.RegisterViewModel
import com.example.talksy.presentation.startCompose.StartCompose

fun NavGraphBuilder.authNav(
    navController: NavHostController,
    onBoardingViewModel: OnBoardingViewModel,
    registerViewModel: RegisterViewModel,
    loginViewModel: LoginViewModel
) {

//    navigation(route = Graph.AUTHENTICATION, startDestination = AuthScreen.OnBoarding.route) {
//        composable(route = AuthScreen.OnBoarding.route) {
//            OnBoarding(
//                navController = navController,
//                state = onBoardingViewModel.state.value,
//                onEvent = onBoardingViewModel::onEvent,
//                events = onBoardingViewModel.events
//            )
//        }
//        composable(route = AuthScreen.Register.route) {
//            Register(
//                navController = navController,
//                state = registerViewModel.state.value,
//                onEvent = registerViewModel::onEvent,
//                events = registerViewModel.events
//            )
//        }
//        composable(route = AuthScreen.Login.route) {
//            Login(
//                navController = navController,
//                state = loginViewModel.state.value,
//                onEvent = loginViewModel::onEvent,
//                events = loginViewModel.events
//            )
//        }
//    }
}

sealed class AuthScreen(val route: String) {
    object OnBoarding : AuthScreen(route = "on_boarding")
    object Login : AuthScreen(route = "login")
    object Register : AuthScreen(route = "register")
}