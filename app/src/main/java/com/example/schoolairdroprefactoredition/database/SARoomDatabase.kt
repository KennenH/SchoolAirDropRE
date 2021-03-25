package com.example.schoolairdroprefactoredition.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blankj.utilcode.util.LogUtils
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
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    it.databaseDao().updateInterruptedMessageStatus()
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