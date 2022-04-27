package hr.algebra.cryptonews.dao

import androidx.room.*
import hr.algebra.cryptonews.model.Item

@Dao
interface NewsDao : BaseDao<Item> {

    @Query("select * from cryptonews")
    override fun getAll(): MutableList<Item>

    @Query("select * from cryptonews where _id=:id")
    override fun get(id: Long): Item?

    @Insert
    override fun insert(item: Item)

    @Update
    override fun update(item: Item)

    @Delete
    override fun delete(item: Item)

}