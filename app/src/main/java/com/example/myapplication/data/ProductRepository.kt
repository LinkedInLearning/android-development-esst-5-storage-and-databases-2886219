package com.example.myapplication.data

import android.app.Application
import android.util.Log
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
        val listType = Types.newParameterizedType(List::class.java, Product::class.java)
        val fileContents = moshi.adapter<List<Product>>(listType)
            .toJson(products)

        val file = File(app.cacheDir, "products.json")
        file.writeText(fileContents)
    }

}