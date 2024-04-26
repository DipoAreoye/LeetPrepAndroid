package com.leetprep.app.data.network

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.leetprep.app.BuildConfig
import com.leetprep.app.data.database.model.Problem
import com.leetprep.app.data.database.model.Submission
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class APIService {
    val supabase = createSupabaseClient(
        "https://sknkqjijesdeaeuukkmq.supabase.co",
        BuildConfig.SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
    }

    val openai = OpenAI(
        token = BuildConfig.OPEN_AI_KEY,
        timeout = Timeout(socket = 60.seconds),
    )

    suspend fun signIn() {
        supabase.auth.signInAnonymously()
    }

    suspend fun getProblems() : List<Problem> {
        return supabase.from("Problem").select().decodeList<Problem>()
    }

    suspend fun submitSolution(submission: Submission) {
        delay(300)

    }
}