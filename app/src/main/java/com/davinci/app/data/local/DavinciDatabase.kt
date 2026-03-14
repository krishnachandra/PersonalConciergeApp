package com.davinci.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davinci.app.data.local.dao.PriceDao
import com.davinci.app.data.local.dao.TaskDao
import com.davinci.app.data.local.entity.PriceEntity
import com.davinci.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, PriceEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class DavinciDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun priceDao(): PriceDao

    companion object {
        val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insert the "Register for Rama Aadhar" task
                val now = System.currentTimeMillis()
                db.execSQL(
                    """
                    INSERT INTO tasks (id, title, category, isUrgent, status, createdAt, sharedWith, assignedToName)
                    VALUES ('rama-aadhar-id', 'Register for Rama Aadhar', 'personal', 1, 'pending', ${now}, 'NPS,JPL', 'NKC')
                    """.trimIndent()
                )
            }
        }
    }
}
