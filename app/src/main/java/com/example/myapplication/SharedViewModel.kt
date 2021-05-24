package com.example.myapplication

import androidx.lifecycle.*
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductRepository

class SharedViewModel : ViewModel() {

    var productRepository: ProductRepository = ProductRepository()

    val selectedProduct: MutableLiveData<Product> = MutableLiveData()

    val products: LiveData<List<Product>> = liveData {
        val data = productRepository.getProducts()
        emit(data)
    }
}