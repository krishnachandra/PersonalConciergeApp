package com.davinci.app.data.repository

import com.davinci.app.data.remote.SupabaseClientProvider
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.UserRepository
import io.github.jan.supabase.gotrue.auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClientProvider,
) : UserRepository {

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val user = supabase.client.auth.currentUserOrNull()
            if (user != null) {
                Result.success(
                    UserProfile(
                        id = user.id,
                        email = user.email ?: "",
                        displayName = user.userMetadata?.get("display_name")?.toString() ?: "User",
                        avatarUrl = user.userMetadata?.get("avatar_url")?.toString(),
                    )
                )
            } else {
                Result.failure(Exception("Not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<UserProfile> {
        // TODO: Update Supabase user metadata + profiles table
        return Result.success(profile)
    }

    override suspend fun getFamilyMembers(): Result<List<FamilyMember>> {
        // TODO: Fetch from Supabase family_members table
        return Result.success(emptyList())
    }

    override suspend fun inviteFamilyMember(email: String): Result<Unit> {
        // TODO: Send invite via Supabase edge function
        return Result.success(Unit)
    }
}
