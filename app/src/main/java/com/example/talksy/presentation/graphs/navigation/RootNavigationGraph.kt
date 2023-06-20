package com.example.talksy.presentation.graphs.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
//import androidx.navigation.NavHost
//import androidx.navigation.NavHostController
//
//@Composable
//fun RootNavigationGraph(navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        route = Graph.ROOT,
//        startDestination = Graph.AUTHENTICATION
//    ) {
//        authNavGraph(navController = navController)
//        composable(route = Graph.HOME) {
//            HomeScreen()
//        }
//    }
//}
//
//object Graph {
//    const val ROOT = "root_graph"
//    const val AUTHENTICATION = "auth_graph"
//    const val HOME = "main_graph"
////    const val DETAILS = "details_graph"
//}