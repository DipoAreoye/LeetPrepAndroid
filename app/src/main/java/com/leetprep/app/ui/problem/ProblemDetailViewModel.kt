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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProblemDetailViewSate {
    data object Loading : ProblemDetailViewSate
    data class Ready (
        val problem: Problem,
        val submission: Submission?,
        val feedback: SubmissionFeedback?,
        val selectedTab: Tab = Tab.PROBELM,
    ) : ProblemDetailViewSate

}
enum class Tab(
    val title: String
) {
    PROBELM(title = "Problem"),
    SOLUTION(title = "Solution")
}

@HiltViewModel(assistedFactory = ProblemDetailViewModel.Factory::class)
class ProblemDetailViewModel @AssistedInject constructor(
    private val problemStore: ProblemStore,
    private val submitSolutionUseCase: SubmitSolutionUseCaseTest,
    @Assisted private val problemId: Long,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(Tab.PROBELM)
    private var _state = MutableStateFlow<ProblemDetailViewSate>(ProblemDetailViewSate.Loading)
    val state: StateFlow<ProblemDetailViewSate>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                _selectedTab,
                problemStore.problemWithId(problemId),
                problemStore.submissionWithProblemId(problemId),
                problemStore.submissionFeedbackWithProblemId(problemId)
            ){ selectedTab, problem, submission, feedback ->
                ProblemDetailViewSate.Ready(
                    problem,
                    submission,
                    feedback,
                    selectedTab
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ProblemDetailViewSate.Loading
            ).collect {
                _state.value = it
            }
        }
    }

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

    fun onTabSelected(tab: Tab) {
        _selectedTab.value = tab
    }
    @AssistedFactory
    interface Factory {
        fun create(problemId: Long): ProblemDetailViewModel
    }
}