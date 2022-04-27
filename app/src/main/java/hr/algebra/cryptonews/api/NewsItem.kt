package hr.algebra.cryptonews.api

import com.google.gson.annotations.SerializedName

data class NewsItem(
    @SerializedName("title") val title: String,
    @SerializedName("articleUrl") val articleUrl: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("datestamp") val date: String,
    @SerializedName("provider") val provider: Provider
)
