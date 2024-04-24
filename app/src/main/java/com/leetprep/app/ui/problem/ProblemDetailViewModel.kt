package com.leetprep.app.ui.problem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leetprep.app.data.ProblemStore
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
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
    ) : ProblemDetailViewSate
}

@HiltViewModel(assistedFactory = ProblemDetailViewModel.Factory::class)
class ProblemDetailViewModel @AssistedInject constructor(
    val problemStore: ProblemStore,
    @Assisted private val problemId: Long,
) : ViewModel() {

    val state : StateFlow<ProblemDetailViewSate> =
     combine(
         problemStore.problemWithId(problemId),
         problemStore.submissionWithProblemId(problemId)
     ){ problem, submission ->
         ProblemDetailViewSate.Ready(
             problem,
             submission
         )
     }.stateIn(
         scope = viewModelScope,
         started = SharingStarted.Eagerly,
         initialValue = ProblemDetailViewSate.Loading
     )

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