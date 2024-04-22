package com.leetprep.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
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
fun ProblemListScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            homeAppBar()
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), userScrollEnabled = true) {
            itemsIndexed(state.problems) {index, item ->
                ProblemListItem(item, index)
            }
        }
    }
}

@Composable
fun homeAppBar() {
    Row(modifier = Modifier
        .background(Color.Black)
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
fun ProblemListItem(problem: Problem, index: Int) {
    val bgColor = if (index % 2 == 0)
        Color.Black else Color.Gray

    val problemColor = when (problem.difficulty) {
        1 -> Color.Green
        2 -> Color.Yellow
        3 -> Color.Red
        else -> Color.White
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(bgColor)
        .padding(12.dp)
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
fun appBarPreview() {
    homeAppBar()
}

@Preview()
@Composable
fun ProblemListPreview() {
    LeetPrepAndroidTheme {
        Column {
            ProblemListItem(problem = Problem(1, "Two Sum", "test", difficulty = 1), 0)
            ProblemListItem(problem = Problem(1, "Valid Palindrome", "test", difficulty = 2), 1)
        }
    }
}