package com.leetprep.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leetprep.app.data.ProblemStore
import com.leetprep.app.data.ProblemsRepository
import com.leetprep.app.data.database.model.Problem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val problemsRepository: ProblemsRepository,
    private val problemsStore: ProblemStore,
) : ViewModel(){

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    init {
        viewModelScope.launch {
            problemsStore.problemsSortedByDifficulty().collect {
                _state.value = HomeViewState(it, false)
            }
        }

        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                problemsRepository.updateProblems()
            }.onFailure {
                Log.e("HomeViewModel", "${it.message}" )
            }
        }
    }

}

data class HomeViewState(
    val problems: List<Problem> = listOf(),
    val refreshing: Boolean = false,
)