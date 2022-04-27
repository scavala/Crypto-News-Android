package hr.algebra.cryptonews.dao

import android.content.Context

fun getCryptoNewsRepository(context: Context) = CryptoNewsSqlHelper(context)