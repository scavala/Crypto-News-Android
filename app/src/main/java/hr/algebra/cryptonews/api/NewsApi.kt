package hr.algebra.cryptonews.api


import hr.algebra.cryptonews.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://crypto-open-news.p.rapidapi.com/"

interface NewsApi {
    @GET("news?rapidapi-key=" + BuildConfig.API_KEY)
    fun fetchItems(@Query("limit") limit: Int): Call<NewsFeed>
}