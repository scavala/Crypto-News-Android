package hr.algebra.cryptonews.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.algebra.cryptonews.model.Item

@Database(entities = [Item::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun personDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(NewsDatabase::class.java) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            "cryptonews.db"
        ).build()
    }
}