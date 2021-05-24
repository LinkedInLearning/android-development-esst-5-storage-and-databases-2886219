package com.example.myapplication

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductRepository

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    var productRepository: ProductRepository = ProductRepository(app)

    val selectedProduct: MutableLiveData<Product> = MutableLiveData()

    val products: LiveData<List<Product>> = liveData {
        val data = productRepository.getProducts()
        emit(data)
    }
}