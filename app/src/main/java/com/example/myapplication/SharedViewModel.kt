package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)
val NUM_BOTTLES = intPreferencesKey("num_of_bottles")

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var productRepository: ProductRepository = ProductRepository(app)

    val selectedProduct: MutableLiveData<Product> = MutableLiveData()

    val products: LiveData<List<Product>> =
        productRepository.getProducts().asLiveData()

    val quantity: LiveData<Int?> = app.dataStore.data.map {
        it[NUM_BOTTLES] ?: 0
    }.asLiveData()

    init {
        viewModelScope.launch {
            productRepository.loadProducts()
        }
    }

    fun incrementQuantity() {
        viewModelScope.launch {
            app.dataStore.edit {
                val currentValue = it[NUM_BOTTLES] ?: 0
                it[NUM_BOTTLES] = currentValue + 1
            }
        }
    }

    fun decrementQuantity() {
        viewModelScope.launch {
            app.dataStore.edit {
                val currentValue = it[NUM_BOTTLES] ?: 0
                if (currentValue > 0) {
                    it[NUM_BOTTLES] = currentValue - 1
                }
            }
        }
    }
}