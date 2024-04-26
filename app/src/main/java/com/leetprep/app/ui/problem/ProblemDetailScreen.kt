package com.leetprep.app.ui.problem

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leetprep.app.data.database.model.FeedbackItem
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
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
                },
                onSubmit = {
                    viewModel.submit(it)
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
    navigateBack: () -> Unit,
    onSubmit: (text: String) -> Unit
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
                            1 -> SubmissionTextField(
                                submission?.text,
                                onUpdateSubmission,
                                onSubmit)
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
    onUpdateText: (text: String) -> Unit,
    onSubmitClick: (text: String) -> Unit,
) {
    var text by remember { mutableStateOf(initialText ?: "") }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value =  text,
            onValueChange = { newText ->
                text = newText
            },
            maxLines = 10,
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    color = Color.LightGray,
                    text = "Write out your solution in natural Language here :)"
                )
            }
        )

        Column(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            HorizontalDivider (
                color = Color.White,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )

            Row(
            ) {
                Spacer(Modifier.weight(1f))
                TextButton(
                    onClick = {
                        Log.d("button", "clicked")
                    }) {
                    Text(
                        color = Color.White,
                        text = "Result"
                    )
                }
                TextButton(
                    onClick = {
                        onSubmitClick(text)
                    }) {
                    Text(
                        color = Color.White,
                        text = "Submit"
                    )
                }
            }
        }



    }

    LaunchedEffect(key1 = text) {
        delay(300) // 300ms debounce
        onUpdateText(text)
    }
}


@Composable
fun FeedbackDialog(
    feedback: FeedbackItem,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier
                    .height(375.dp)
                    .fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentColor = Color.White,
                    color = Color.Black,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = feedback.title)
                        Text(text = feedback.message)
                    }
                }
            }
    }
}

@Preview
@Composable
fun SubmissionTextPreview() {
    SubmissionTextField(
        initialText = "This is a test Teextfield",
        onUpdateText = {},
        onSubmitClick = {}
    )
}

@Preview
@Composable
fun FeedbackDialogPreview() {
    FeedbackDialog(
        FeedbackItem(
            title = "Use a hashmap",
            message = "This is a nonsense message and " +
                    "doesn't mean anything, but please use a hashmap"
        )
    ) {
    }
}


