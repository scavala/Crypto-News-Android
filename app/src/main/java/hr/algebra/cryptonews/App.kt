package hr.algebra.cryptonews

import android.app.Application
import hr.algebra.cryptonews.dao.NewsDao
import hr.algebra.cryptonews.dao.NewsDatabase

class App : Application() {

    private lateinit var newsDao: NewsDao

    override fun onCreate() {
        super.onCreate()
        newsDao = NewsDatabase.getInstance(this).personDao()
    }

    fun getNewsDao() = newsDao
}