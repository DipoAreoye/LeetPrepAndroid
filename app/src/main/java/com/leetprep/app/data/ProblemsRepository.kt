package com.leetprep.app.data

import android.util.Log
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.network.APIService
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProblemsRepository @Inject constructor(
    private val problemStore: ProblemStore,
    private val apiService: APIService
) {

    suspend fun updateProblems() {
        val problems = apiService.supabase.from("Problem").select().decodeList<Problem>()
        withContext(Dispatchers.IO) {
            problemStore.addProblems(problems)
        }
    }
}