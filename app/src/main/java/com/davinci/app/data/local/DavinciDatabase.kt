package com.davinci.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davinci.app.data.local.dao.PriceDao
import com.davinci.app.data.local.dao.TaskDao
import com.davinci.app.data.local.entity.PriceEntity
import com.davinci.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, PriceEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DavinciDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun priceDao(): PriceDao
}
