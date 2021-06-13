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

    suspend fun getProducts(): List<Product> {
        val productsFromCache = readDataFromFile()
        if (productsFromCache.isNotEmpty()) {
            Log.i(LOG_TAG, "loaded from cache")
            return productsFromCache
        }

        val response = productApi.getProducts()
        return if (response.isSuccessful) {
            Log.i(LOG_TAG, "loaded from webservice")
            val products = response.body() ?: emptyList()
            storeDataInFile(products)

            products
        } else
            emptyList()
    }

    private fun storeDataInFile(products: List<Product>?) {
        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
            val listType = Types.newParameterizedType(List::class.java, Product::class.java)
            val fileContents = moshi.adapter<List<Product>>(listType)
                .toJson(products)

            val file = File(
                app.getExternalFilesDir("products"),
                "products.json"
            )
            file.writeText(fileContents)
        }
    }

    private fun readDataFromFile(): List<Product> {
        val file = File(
            app.getExternalFilesDir("products"),
            "products.json"
        )
        val json = if (file.exists()) file.readText() else null

        return if (json == null)
            emptyList()
        else {
            val listType = Types.newParameterizedType(List::class.java, Product::class.java)
            moshi.adapter<List<Product>>(listType).fromJson(json) ?: emptyList()
        }
    }

}