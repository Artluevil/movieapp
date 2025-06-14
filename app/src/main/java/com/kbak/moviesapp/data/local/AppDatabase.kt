package com.kbak.moviesapp.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [GenreEntity::class, MovieEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("PRAGMA foreign_keys=ON;")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Create the new table with localId
        db.execSQL("""
            CREATE TABLE movies_new (
                localId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id INTEGER NOT NULL,
                title TEXT NOT NULL,
                originalTitle TEXT NOT NULL,
                overview TEXT NOT NULL,
                posterPath TEXT NOT NULL,
                backdropPath TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                voteAverage REAL NOT NULL,
                voteCount INTEGER NOT NULL,
                popularity REAL NOT NULL,
                originalLanguage TEXT NOT NULL,
                adult INTEGER NOT NULL,
                video INTEGER NOT NULL,
                genreIds TEXT NOT NULL
            )
        """.trimIndent())

        // 2. Copy data from the old table into the new one
        db.execSQL("""
            INSERT INTO movies_new (
                id, title, originalTitle, overview, posterPath, backdropPath,
                releaseDate, voteAverage, voteCount, popularity, originalLanguage,
                adult, video, genreIds
            )
            SELECT 
                id, title, originalTitle, overview, posterPath, backdropPath,
                releaseDate, voteAverage, voteCount, popularity, originalLanguage,
                adult, video, genreIds
            FROM movies
        """.trimIndent())

        // 3. Drop the old table
        db.execSQL("DROP TABLE movies")

        // 4. Rename the new table
        db.execSQL("ALTER TABLE movies_new RENAME TO movies")
    }
}

