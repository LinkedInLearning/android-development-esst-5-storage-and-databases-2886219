package com.example.myapplication.shop

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.data.Product
import com.example.myapplication.databinding.ProductItemBinding

class ProductAdapter(private val items: List<Product>, private val onItemClick: (Product) -> Unit)
    : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ProductItemBinding)
            : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]

        with(holder.binding) {
            productImage.load(product.imageFile) {
                crossfade(true)
            }
            productNameText.text = product.name
            sizeText.text = sizeText.context.resources.getString(
                R.string.product_size_label,
                product.size
            )
            priceText.text = NumberFormat.getCurrencyInstance().format(product.price)
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = items.size
}