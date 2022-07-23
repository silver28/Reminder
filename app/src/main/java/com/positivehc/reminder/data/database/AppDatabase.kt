package com.positivehc.reminder.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EventDbModel::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val dbName = "reminder.db"

        fun getInstance(application: Application): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application,
                        AppDatabase::class.java,
                        dbName
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}