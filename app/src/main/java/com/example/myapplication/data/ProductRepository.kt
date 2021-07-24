package com.example.myapplication.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myapplication.LOG_TAG
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

const val BASE_ENDPOINT_URL = "https://2873199.youcanlearnit.net/"

class ProductRepository(private val app: Application) {

    private val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val productApi: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }

    private val productDao = ProductDatabase.getDatabase(app)
        .productDao()

    fun getProducts(): Flow<List<Product>> {
        return productDao.getProducts()
    }

    suspend fun loadProducts() {
        if (productDao.getCount() <= 0) {
            val response = productApi.getProducts()
            if (response.isSuccessful) {
                Log.i(LOG_TAG, "loaded from webservice")
                val products = response.body() ?: emptyList()
                storeDataInDb(products)
            }
        }
    }

    private suspend fun storeDataInDb(products: List<Product>?) {
        if (products != null) {
            productDao.insertProducts(products)
        }
    }

}