package com.example.myapplication

import android.app.Application
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductRepository

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var productRepository: ProductRepository = ProductRepository(app)

    val selectedProduct: MutableLiveData<Product> = MutableLiveData()

    val products: LiveData<List<Product>> = liveData {
        val data = productRepository.getProducts()
        emit(data)
    }

    val quantity: MutableLiveData<Int> = MutableLiveData(
        PreferenceManager.getDefaultSharedPreferences(app)
            .getInt("num_of_bottles", 0)
    )

    fun incrementQuantity() {
        quantity.value = quantity.value!! + 1

        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putInt("num_of_bottles", quantity.value!!)
            .apply()
    }
}