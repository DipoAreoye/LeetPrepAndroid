package com.leetprep.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leetprep.app.ui.home.ProblemListScreen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object ProblemDetail: Screen("problem/${PROBLEM_ID}")
    companion object {
        val PROBLEM_ID = "problemID"
    }
}
@Composable
fun LeetPrepApp () {
    NavHost(
        navController = rememberNavController(),
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) { backStackEntry ->
            ProblemListScreen()
        }
    }

}