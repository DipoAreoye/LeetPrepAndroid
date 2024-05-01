package com.leetprep.app.ui.problem

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leetprep.app.R
import com.leetprep.app.data.database.model.FeedbackItem
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.SolutionRating
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProblemDetailScreen(
    viewModel: ProblemDetailViewModel,
    navigateBack: () -> Unit,
    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    when (val s = state) {
        is ProblemDetailViewSate.Loading -> {

        }
        is ProblemDetailViewSate.Ready -> {
            ProblemDetailScreen(
                s.problem,
                s.submission,
                s.feedback,
                navigateBack = navigateBack,
                selectedTab = s.selectedTab,
                onTabSelected = {
                    viewModel.onTabSelected(it)
                    val page = when(it) {
                        Tab.PROBLEM -> 0
                        Tab.SOLUTION -> 1
                    }
                    scope.launch {
                        pagerState.scrollToPage(page)
                    }
                },
                pagerState = pagerState,
                onUpdateSubmission = {
                    viewModel.updateSubmission(it)
                },
                isSubmitting = s.submitting,
                onSubmit = {
                    viewModel.submit()
                }
            )
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .flowOn(Dispatchers.Default) // Switch to a background thread
            .collectLatest { page ->
                val tab = when (page) {
                    0 -> Tab.PROBLEM
                    else -> Tab.SOLUTION
                }
                withContext(Dispatchers.Main) { // Switch back to the main thread for UI updates
                    viewModel.onTabSelected(tab)
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProblemDetailScreen(
    problem: Problem,
    submission: Submission?,
    submissionFeedback: SubmissionFeedback?,
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    pagerState: PagerState,
    onUpdateSubmission: (text: String) -> Unit,
    navigateBack: () -> Unit,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
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
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = Color.White) {
                Column {
                    ProblemSolutionTabs(selectedTab, onTabSelected)
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> ProblemDescription(problem = problem)
                            1 -> SubmissionTextField(
                                submission?.text,
                                submissionFeedback,
                                isSubmitting,
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
fun ProblemSolutionTabs(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit
) {
    val tabs = listOf(Tab.PROBLEM, Tab.SOLUTION )

    TabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        tabs.map { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = {
                    onTabSelected(tab)
                },
                text = {
                    Text(
                        text = tab.title,
                        modifier = Modifier.padding()
                    )
                }
            )
        }

    }
}

@Composable
fun ProblemDescription(problem: Problem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(
                start = 0.dp,
                top = 8.dp,
                end = 0.dp,
                bottom = 16.dp
            ),
            fontWeight = FontWeight.Bold,
            text = problem.getDifficultyString()
        )
        Text(problem.desc)
    }
}

@Composable
fun SubmissionTextField (
    initialText: String?,
    submissionFeedback: SubmissionFeedback?,
    isSubmitting: Boolean,
    onUpdateText: (text: String) -> Unit,
    onSubmitClick: () -> Unit,
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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            FeedbackCard(
                isSubmitting = isSubmitting,
                submissionFeedback = submissionFeedback,
                onSubmitClick = onSubmitClick
            )
        }
    }

    LaunchedEffect(key1 = text) {
        delay(300) // 300ms debounce
        onUpdateText(text)
    }
}

@Composable
fun FeedbackCard(
    modifier: Modifier = Modifier,
    isSubmitting: Boolean,
    submissionFeedback: SubmissionFeedback? = null,
    onSubmitClick: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val titleText = when (submissionFeedback?.getSolutionRating()) {
        SolutionRating.CORRECT -> "Solution Correct"
        SolutionRating.NEEDS_IMPROVEMENT -> "Solution Accepted"
        SolutionRating.INCORRECT -> "Solution Incorrect"
        else -> ""
    }
    val titleColor = when (submissionFeedback?.getSolutionRating()) {
        SolutionRating.CORRECT -> Color.Green
        SolutionRating.NEEDS_IMPROVEMENT -> Color.Yellow
        SolutionRating.INCORRECT -> Color.Red
        else -> Color.White
    }

    val feedback = submissionFeedback?.feedbackItems?.first()

    Card(
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            contentColor = Color.White,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        IconButton(onClick = { onSubmitClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "send"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = titleText,
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            if (feedback != null) {
                                isExpanded = !isExpanded
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down),
                            contentDescription = if (isExpanded) "minimize" else "expand",
                            modifier = Modifier.rotate(
                                if (isExpanded) 0f else 180f
                            )
                        )
                    }
                }

                AnimatedVisibility(visible = isExpanded && feedback != null) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = feedback!!.title
                        )
                        Text(text = feedback.message)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SubmissionTextPreview() {
    SubmissionTextField(
        initialText = "This is a test Textfield",
        submissionFeedback = null,
        onUpdateText = {},
        isSubmitting = false,
        onSubmitClick = {}
    )
}

@Preview
@Composable
fun FeedbackDialogPreview() {
    FeedbackCard(
        modifier = Modifier,
        isSubmitting = true,
        onSubmitClick = {}
    )
}


