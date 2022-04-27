package hr.algebra.cryptonews.api


import android.content.Context
import android.util.Log
import hr.algebra.cryptonews.App
import hr.algebra.cryptonews.broadcast_receiver.CryptoNewsReceiver
import hr.algebra.cryptonews.DATA_IMPORTED
import hr.algebra.cryptonews.framework.sendBroadcast
import hr.algebra.cryptonews.framework.setBooleanPreference
import hr.algebra.cryptonews.handler.downloadImageAndStore
import hr.algebra.cryptonews.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val limit = 20

class NewsFetcher(private val context: Context) {

    private var newsApi: NewsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApi::class.java)
    }

    fun fetchItems() {

        val request = newsApi.fetchItems(limit)
        request.enqueue(object : Callback<NewsFeed> {
            override fun onResponse(
                call: Call<NewsFeed>,
                response: Response<NewsFeed>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<NewsFeed>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(fetchedItems: NewsFeed) {
        GlobalScope.launch {

            fetchedItems.news.forEach {
                val picturePath =
                    downloadImageAndStore(context, it.imageUrl, it.title.replace(" ", "_"))
                (context.applicationContext as App).getNewsDao().insert(
                    Item(
                        null,
                        it.title,
                        it.articleUrl,
                        it.description,
                        picturePath ?: "",
                        it.date,
                        it.provider.name,
                        false
                    )
                )

                /* val values = ContentValues().apply {
                     put(Item::title.name, it.title)
                     put(Item::articleUrl.name, it.articleUrl)
                     put(Item::description.name, it.description)
                     put(Item::picturePath.name, picturePath?: "")
                     put(Item::date.name, it.date)
                     put(Item::provider.name, it.provider.name)
                     put(Item::favorite.name, false)

                 }

                 context.contentResolver.insert(CRYPTONEWS_PROVIDER_URI, values)
                 //sqlLite version
                 */
            }

            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<CryptoNewsReceiver>()
        }
    }


}