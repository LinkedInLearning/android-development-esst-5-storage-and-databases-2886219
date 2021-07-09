package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding

const val LOG_TAG = "two_trees_oil"

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) return

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        viewModel.quantity.observe(this, {
            if (it != null) {
                Log.i(LOG_TAG, "updating the badge with this count: $it")
                updateBadge(it)
            }
        })
    }

    private fun updateBadge(count: Int) {
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.action_shop)
        if (count > 0) {
            badge.number = count
            badge.isVisible = true
        } else {
            badge.clearNumber()
            badge.isVisible = false
        }
    }

}