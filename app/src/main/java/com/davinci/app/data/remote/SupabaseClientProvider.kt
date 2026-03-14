package com.davinci.app.data.remote

import com.davinci.app.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Supabase client singleton providing access to all Supabase services.
 *
 * Configuration:
 * - URL and anon key are injected from BuildConfig (set in app/build.gradle.kts)
 * - Auth module for login/signup/session management
 * - Postgrest module for database CRUD
 * - Realtime module for live task sync
 * - Storage module for avatar uploads
 */
@Singleton
class SupabaseClientProvider @Inject constructor() {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }
}
