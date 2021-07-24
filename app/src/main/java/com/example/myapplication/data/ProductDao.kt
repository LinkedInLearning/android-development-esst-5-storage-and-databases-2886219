package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // TODO: Add the correct annotation
    fun getTotalQuantity(): Flow<Int>

    // TODO: Add the correct annotation
    suspend fun updateProduct(product: Product)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<Product>>

    @Insert
    suspend fun insertProducts(products: List<Product>)
}