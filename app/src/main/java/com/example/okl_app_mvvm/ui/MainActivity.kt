package com.example.okl_app_mvvm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.ActivityMainBinding
import com.example.okl_app_mvvm.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToTrackingFragmentIfNeeded(intent)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.homeFragment, R.id.gamesFragment ->
                        binding.bottomNavigation.visibility = View.VISIBLE
                    else -> binding.bottomNavigation.visibility = View.GONE
                }
            }
        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.homeFragment ->
                        binding.bottomNavigation.background.setTint(resources.getColor(R.color.dark_text))
                    R.id.gamesFragment ->
                        binding.bottomNavigation.background.setTint(resources.getColor(R.color.oklcolor_darker_inv))
                    else -> binding.bottomNavigation.visibility = View.GONE
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }


    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?){
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            navHostFragment.findNavController().navigate(R.id.gamesFragment)
            navController.navigate(R.id.action_gamesFragment_to_visitingParkFragment)
        }
    }

}