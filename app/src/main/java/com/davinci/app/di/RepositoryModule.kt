package com.davinci.app.di

import com.davinci.app.data.repository.AuthRepositoryImpl
import com.davinci.app.data.repository.InvestmentRepositoryImpl
import com.davinci.app.data.repository.TaskRepositoryImpl
import com.davinci.app.data.repository.UserRepositoryImpl
import com.davinci.app.domain.repository.AuthRepository
import com.davinci.app.domain.repository.InvestmentRepository
import com.davinci.app.domain.repository.TaskRepository
import com.davinci.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindInvestmentRepository(impl: InvestmentRepositoryImpl): InvestmentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
