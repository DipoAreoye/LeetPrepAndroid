package com.leetprep.app.data.network

import com.leetprep.app.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest

class APIService {
    val supabase = createSupabaseClient(
        "https://sknkqjijesdeaeuukkmq.supabase.co",
        BuildConfig.SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
    }

    suspend fun signIn() {
        supabase.auth.signInAnonymously()
    }
}