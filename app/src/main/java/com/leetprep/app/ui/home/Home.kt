package com.leetprep.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.ui.theme.LeetPrepAndroidTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToProblemDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            homeAppBar()
        }
    ) { padding ->
        ProblemList(
            padding,
            state.problems,
            navigateToProblemDetail
        )
    }
}

@Composable
fun homeAppBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = "Problems",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
    }
}
@Composable
fun ProblemList(
    padding: PaddingValues,
    problems: List<Problem>,
    navigateToProblemDetail: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(padding),
        userScrollEnabled = true) {
        itemsIndexed(problems) {index, item ->
            ProblemListItem(item, index, navigateToProblemDetail)
        }
    }
}

@Composable
fun ProblemListItem(problem: Problem, index: Int, navigateToProblem: (Int) -> Unit) {
    val bgColor = if (index % 2 == 0)
        MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.primaryContainer

    val problemColor = when (problem.difficulty) {
        1 -> Color.Green
        2 -> Color.Yellow
        3 -> Color.Red
        else -> Color.White
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = bgColor)
        .padding(12.dp)
        .clickable {
            navigateToProblem(problem.id)
        }
    ) {
        Column(modifier = Modifier
            .weight(2f),
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 4.dp),
                text = problem.title,
                color = Color.White
            )
        }
        Column(modifier = Modifier
            .weight(1f),
        ) {
            Text(
                text = problem.getDifficultyString(),
                color = problemColor
            )
        }
    }
}

@Preview
@Composable
fun homeAppBarPreview() {
    homeAppBar()
}


@Preview
@Composable
fun problemList() {
    val problem1 = Problem(
        id = 0,
        title = "Two Sum",
        difficulty = 1,
        desc = "bla bla",
    )

    val problem2 = Problem(
        id = 0,
        title = "Valid Parenthesis",
        difficulty = 1,
        desc = "bla bla",
    )


    val problems = listOf(problem1, problem2)

    LeetPrepAndroidTheme {
        Surface(tonalElevation = 5.dp){
            ProblemList(padding = PaddingValues(), problems = problems, {})
        }
    }
}

