package hr.algebra.cryptonews.api

import com.google.gson.annotations.SerializedName

data class NewsFeed(
    @SerializedName("news") val news: List<NewsItem>
)
