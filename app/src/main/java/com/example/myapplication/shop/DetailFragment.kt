package com.example.myapplication.shop

import android.icu.text.NumberFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.myapplication.LOG_TAG
import com.example.myapplication.R
import com.example.myapplication.SharedViewModel
import com.example.myapplication.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
           with(product) {
               binding.productImage.load(imageFile)
               binding.productNameText.text = name
               binding.descriptionText.text = description
               binding.sizeText.text = getString(R.string.product_size_label, size)
               binding.priceText.text = NumberFormat.getCurrencyInstance().format(price)
           }
        })

        binding.addToCartButton.setOnClickListener {
            Log.i(LOG_TAG, "clicked the add to cart button")
            viewModel.incrementQuantity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}