package com.davinci.app.di

import android.content.Context
import androidx.room.Room
import com.davinci.app.data.local.DavinciDatabase
import com.davinci.app.data.local.dao.PriceDao
import com.davinci.app.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DavinciDatabase {
        return Room.databaseBuilder(
            context,
            DavinciDatabase::class.java,
            "davinci_db"
        )
        .addCallback(DavinciDatabase.CALLBACK)
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTaskDao(db: DavinciDatabase): TaskDao = db.taskDao()

    @Provides
    fun providePriceDao(db: DavinciDatabase): PriceDao = db.priceDao()
}
