package com.example.myapplication.shop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.LOG_TAG
import com.example.myapplication.R
import com.example.myapplication.SharedViewModel
import com.example.myapplication.data.Product
import com.example.myapplication.databinding.FragmentShopBinding
import com.google.android.material.snackbar.Snackbar

class ShopFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Snackbar.make(
                    binding.root,
                    R.string.storage_permission_granted,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

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

        checkStoragePermission()

        viewModel.products.observe(viewLifecycleOwner, { products ->
            binding.productList.adapter = ProductAdapter(products, onItemClick)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

}