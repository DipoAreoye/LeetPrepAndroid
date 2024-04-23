package com.leetprep.app.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leetprep.app.ui.home.HomeScreen
import com.leetprep.app.ui.problem.ProblemDetailScreen
import com.leetprep.app.ui.problem.ProblemDetailViewModel

sealed class Screen(val route: String) {
    data object Home: Screen("home")
    data object ProblemDetail: Screen("problem/{$PROBLEM_ID}") {
        fun createRoute(problemId: String) = "problem/$problemId"
    }
    companion object {
        val PROBLEM_ID = "problemID"
    }
}
@Composable
fun LeetPrepApp () {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navigateToProblemDetail = { problemId ->
                val encodedUri = Uri.encode(problemId.toString())
                navController.navigate(Screen.ProblemDetail.createRoute(encodedUri))
            })
        }

        composable(route = Screen.ProblemDetail.route) {backstackEntry ->
            val problemId = backstackEntry.arguments?.getString(Screen.PROBLEM_ID)
            val problemDetailViewModel =
                hiltViewModel<ProblemDetailViewModel, ProblemDetailViewModel.Factory>(
                ) {
                    it.create(problemId!!.toLong())
                }
            ProblemDetailScreen(
                viewModel = problemDetailViewModel,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}