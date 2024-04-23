package com.leetprep.app.ui.problem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission


@Composable
fun ProblemDetailScreen(
    viewModel: ProblemDetailViewModel,
    navigateBack: () -> Unit,
    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val s = state) {
        is ProblemDetailViewSate.Loading -> {

        }
        is ProblemDetailViewSate.Ready -> {
            ProblemDetailScreen(
                s.problem,
                s.submission,
                navigateBack = navigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemDetailScreen(
    problem: Problem,
    submission: Submission?,
    navigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(problem.title)
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }

    ) {contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Surface(contentColor = Color.White) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 0.dp,
                            top = 8.dp,
                            end = 0.dp,
                            bottom = 16.dp
                        ),
                        text = problem.getDifficultyString()
                    )
                    Text(problem.desc)
                }
            }
        }

    }

}

