package com.example.okl_app_mvvm.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentSettingsBinding
import com.example.okl_app_mvvm.databinding.FragmentVisitingParkBinding
import com.example.okl_app_mvvm.db.SigningEntity
import com.example.okl_app_mvvm.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restartPoints.setOnClickListener {
            restartDataFromSharedPref()
        }

        binding.infoApp.setOnClickListener(){
            findNavController().navigate(R.id.action_settingsFragment_to_informationFragment)
        }
    }

    private fun restartDataFromSharedPref() {
        sharedPreferences.edit()
            .clear()
            .apply()

        viewModel.insertSigning(resetData())

        Toast.makeText(requireContext(), resources.getString(R.string.restarted), Toast.LENGTH_SHORT).show()
    }

    private fun resetData(): SigningEntity {
        val roza = ""
        val tulipan = ""
        val przebisnieg = ""
        val signingEntity = SigningEntity(roza, tulipan, przebisnieg)
        return signingEntity
    }
}