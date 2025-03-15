package com.kbak.moviesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GenreEntity::class], version = 1, exportSchema = true)
abstract class GenreDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    companion object {
        @Volatile
        private var INSTANCE: GenreDatabase? = null

        fun getDatabase(context: Context): GenreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GenreDatabase::class.java,
                    "genre_database"
                )
                    .setJournalMode(JournalMode.TRUNCATE) // Prevents corruption
                    .fallbackToDestructiveMigration() // Automatically handles version upgrades
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
