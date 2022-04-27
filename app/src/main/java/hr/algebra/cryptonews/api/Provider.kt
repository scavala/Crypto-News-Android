package hr.algebra.cryptonews.api

import com.google.gson.annotations.SerializedName

data class Provider(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)