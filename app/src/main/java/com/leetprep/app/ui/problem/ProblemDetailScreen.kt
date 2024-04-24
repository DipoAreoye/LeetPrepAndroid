package com.leetprep.app.ui.problem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import kotlinx.coroutines.delay


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
                navigateBack = navigateBack,
                onUpdateSubmission = {
                    viewModel.updateSubmission(it)
                }

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProblemDetailScreen(
    problem: Problem,
    submission: Submission?,
    onUpdateSubmission: (text: String) -> Unit,
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
                    val pagerState = rememberPagerState(pageCount = { 2 })
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> ProblemDescription(problem = problem)
                            1 -> SubmissionTextField(submission?.text, onUpdateSubmission)
                        }
                    }

                }
            }
        }

    }

}

@Composable
fun ProblemDescription(problem: Problem) {
    Column(
        modifier = Modifier.fillMaxSize()
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

@Composable
fun SubmissionTextField (
    initialText: String?,
    onUpdateText: (text: String) -> Unit
) {
    var text by remember { mutableStateOf(initialText ?: "") }

    TextField(
        modifier = Modifier.fillMaxSize(),
        value =  text,
        onValueChange = { newText ->
            text = newText
        },
        maxLines = 10,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        placeholder = {
            Text(
                color = Color.LightGray,
                text = "Write out your solution in natural Language here :)"
            )
        }
    )

    LaunchedEffect(key1 = text) {
        delay(300) // 300ms debounce
        onUpdateText(text)
    }
}



