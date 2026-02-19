package com.makur.ideaflash.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Idea::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverters::class)
abstract class IdeaDatabase : RoomDatabase() {
    abstract fun ideaDao(): IdeaDao

    companion object {
        @Volatile
        private var INSTANCE: IdeaDatabase? = null

        fun getDatabase(context: Context): IdeaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IdeaDatabase::class.java,
                    "idea_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}