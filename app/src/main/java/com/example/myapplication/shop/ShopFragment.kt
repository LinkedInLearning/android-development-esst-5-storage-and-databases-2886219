package com.example.myapplication.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.LOG_TAG
import com.example.myapplication.R
import com.example.myapplication.SharedViewModel
import com.example.myapplication.data.Product
import com.example.myapplication.databinding.FragmentShopBinding

class ShopFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val onItemClick: (Product) -> Unit = { product ->
        Log.i(LOG_TAG, "the selected product: $product")
        viewModel.selectedProduct.value = product
        findNavController().navigate(R.id.action_shopFragment_to_detailFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.products.observe(viewLifecycleOwner, { products ->
            binding.productList.adapter = ProductAdapter(products, onItemClick)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}