package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "products")
data class Product(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        var quantity: Int = 0,
        @Json(name = "productName") val name: String,
        val imageFile: String,
        val description: String,
        val size: Int,
        val price: Double
)