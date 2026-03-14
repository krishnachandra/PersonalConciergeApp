package com.davinci.app.data.repository

import com.davinci.app.data.remote.SupabaseClientProvider
import com.davinci.app.domain.model.FamilyRole
import com.davinci.app.domain.model.UserProfile
import com.davinci.app.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClientProvider,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        return try {
            supabase.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val user = supabase.client.auth.currentUserOrNull()
            if (user != null) {
                Result.success(
                    UserProfile(
                        id = user.id,
                        email = user.email ?: email,
                        displayName = user.userMetadata?.get("display_name")?.toString() ?: "User",
                    )
                )
            } else {
                Result.failure(Exception("Login failed — no user returned"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, displayName: String): Result<UserProfile> {
        return try {
            supabase.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val user = supabase.client.auth.currentUserOrNull()
            if (user != null) {
                Result.success(
                    UserProfile(
                        id = user.id,
                        email = email,
                        displayName = displayName,
                    )
                )
            } else {
                Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(): Result<UserProfile> {
        return try {
            supabase.client.auth.signInWith(Google)
            val user = supabase.client.auth.currentUserOrNull()
            if (user != null) {
                Result.success(
                    UserProfile(
                        id = user.id,
                        email = user.email ?: "",
                        displayName = user.userMetadata?.get("full_name")?.toString() ?: "User",
                    )
                )
            } else {
                Result.failure(Exception("Google sign-in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            supabase.client.auth.signOut()
        } catch (_: Exception) { }
    }

    override suspend fun getCurrentUser(): UserProfile? {
        val user = supabase.client.auth.currentUserOrNull() ?: return null
        return UserProfile(
            id = user.id,
            email = user.email ?: "",
            displayName = user.userMetadata?.get("display_name")?.toString() ?: "User",
        )
    }

    override fun isLoggedIn(): Flow<Boolean> = flow {
        emit(supabase.client.auth.currentUserOrNull() != null)
    }
}
