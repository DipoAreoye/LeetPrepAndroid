package com.leetprep.app.ui.problem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leetprep.app.data.ProblemStore
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import com.leetprep.app.data.database.model.SubmissionFeedback
import com.leetprep.app.data.domain.SubmitSolutionUseCase
import com.leetprep.app.data.domain.SubmitSolutionUseCaseTest
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProblemDetailViewSate {
    data object Loading : ProblemDetailViewSate
    data class Ready (
        val problem: Problem,
        val submission: Submission?,
        val feedback: SubmissionFeedback?
    ) : ProblemDetailViewSate
}

@HiltViewModel(assistedFactory = ProblemDetailViewModel.Factory::class)
class ProblemDetailViewModel @AssistedInject constructor(
    private val problemStore: ProblemStore,
    private val submitSolutionUseCase: SubmitSolutionUseCaseTest,
    @Assisted private val problemId: Long,
) : ViewModel() {

    val state : StateFlow<ProblemDetailViewSate> =
     combine(
         problemStore.problemWithId(problemId),
         problemStore.submissionWithProblemId(problemId),
         problemStore.submissionFeedbackWithProblemId(problemId)
     ){ problem, submission, feedback ->
         ProblemDetailViewSate.Ready(
             problem,
             submission,
             feedback
         )
     }.stateIn(
         scope = viewModelScope,
         started = SharingStarted.Eagerly,
         initialValue = ProblemDetailViewSate.Loading
     )

    fun submit(text: String) {
        val s = state.value
        if (s is ProblemDetailViewSate.Ready) {
            viewModelScope.launch {
                submitSolutionUseCase(
                    Submission(
                        text = text,
                        problemId = problemId
                    ),
                    s.problem.desc
                )
            }
        }
    }
    fun updateSubmission(text: String) {
        val currentState = state.value
        if (currentState is ProblemDetailViewSate.Ready) {
            val newSubmission = when {
                 currentState.submission != null -> {
                    val submission = currentState.submission
                    submission.copy(text = text)
                }
                else -> {
                    Submission(
                        text = text,
                        problemId = problemId
                    )
                }
            }

            viewModelScope.launch {
                problemStore.updateSubmission(newSubmission)
            }
        }
    }
    @AssistedFactory
    interface Factory {
        fun create(problemId: Long): ProblemDetailViewModel
    }
}