package hr.algebra.cryptonews.model

import hr.algebra.cryptonews.framework.formatDate
import java.util.*

data class Message(
    val senderId: String = "",
    val message: String = "",
    val photoUrl: String = "",
    val dateTime: String = Date().formatDate()
)
