package com.example.schoolairdroprefactoredition.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.schoolairdroprefactoredition.database.dao.DatabaseDao
import com.example.schoolairdroprefactoredition.database.pojo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [
    ChatHistory::class,
    ChatOfflineNum::class,
    UserCache::class,
    PullFlag::class,
    Favorite::class,
    PurchasingCache::class],
        views = [ChatOfflineNumDetail::class],
        version = 19,
        exportSchema = true)
abstract class SARoomDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    // 每次app打开的时候都会做的事情
//                    it.clearAllTables()
                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SARoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SARoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SARoomDatabase::class.java,
                        "school_airdrop_database"
                ).addCallback(DatabaseCallback(scope))
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}