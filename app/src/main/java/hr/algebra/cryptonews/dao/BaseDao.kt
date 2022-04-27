package hr.algebra.cryptonews.dao

interface BaseDao<T> {
    fun getAll(): MutableList<T>
    fun get(id: Long): T?
    fun insert(item: T)
    fun update(item: T)
    fun delete(item: T)
}