package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProducts(products: List<Product>)
}