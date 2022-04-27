package hr.algebra.cryptonews.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptonews")
data class Item(
    @PrimaryKey(autoGenerate = true) var _id: Long? = null,
    val title: String,
    val articleUrl: String,
    val description: String,
    val picturePath: String,
    val date: String,
    val provider: String,
    var favorite: Boolean
)
