package com.example.okl_app_mvvm.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentSigningFlowersBinding
import com.example.okl_app_mvvm.db.SigningEntity
import com.example.okl_app_mvvm.other.Constants
import com.example.okl_app_mvvm.services.TrackingService
import com.example.okl_app_mvvm.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.checkerframework.checker.signedness.qual.Constant
import javax.inject.Inject
import kotlin.math.sign

@AndroidEntryPoint
class SigningFlowersFragment : Fragment(R.layout.fragment_signing_flowers) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()  //dagger dostarcza viewmodel

    private var _binding: FragmentSigningFlowersBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigningFlowersBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var list_count: Int

        lifecycleScope.launch {
            viewModel.getAllFlowers.collect {
                val list = ArrayList(it)
                if (list.isNotEmpty())
                {
                    list_count = list.count()-1
                    val first = list[list_count]
                    if (first.roza.isNotEmpty()){
                        binding.roza.setText(first.roza)
                    }
                    if (first.tulipan.isNotEmpty()){
                        binding.tulipan.setText(first.tulipan)
                    }
                    if (first.przebisnieg.isNotEmpty()){
                        binding.przebisnieg.setText(first.przebisnieg)
                    }
                }
            }
        }

        binding.floatinButtonFlowers.setOnClickListener {
            val data = getDataFromText()
            viewModel.insertSigning(data)
            checkAnswer(data)
            it.hideKeyboard()
        }

        binding.howToPlay.setOnClickListener(){
            textSize()
        }
    }

    private fun checkAnswer(signingEntity: SigningEntity){

        if (signingEntity.roza.lowercase() == "fiolek wonny" || signingEntity.roza.lowercase() == "fiołek wonny" || signingEntity.roza.lowercase() == "fiołek" || signingEntity.roza.lowercase() == "fiolek"){
            var a = sharedPreferences.getBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_ROZA, false)
            if (!a) {
                getAndAddOverallPoints()
                sharedPreferences.edit()
                    .putBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_ROZA, true)
                    .apply()
                Toast.makeText(context, resources.getString(R.string.gj), Toast.LENGTH_SHORT).show()
            }
            else {

            }
        }
        if (signingEntity.tulipan.lowercase() == "zawilec gajowy" || signingEntity.tulipan.lowercase() == "zawilec"){
            var a = sharedPreferences.getBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_TULIPAN, false)
            if (!a) {
                getAndAddOverallPoints()
                sharedPreferences.edit()
                    .putBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_TULIPAN, true)
                    .apply()
                Toast.makeText(context, resources.getString(R.string.gj), Toast.LENGTH_SHORT).show()
            }
            else {

            }
        }

        if (signingEntity.przebisnieg.lowercase() == "kalina" || signingEntity.przebisnieg.lowercase() == "kalina koralowa")
        {
            var a = sharedPreferences.getBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_PRZEBISNIEG, false)
            if (!a) {
                getAndAddOverallPoints()
                sharedPreferences.edit()
                    .putBoolean(Constants.KEY_SHAREDPREFERENCES_VISITING_PRZEBISNIEG, true)
                    .apply()
                Toast.makeText(context, resources.getString(R.string.gj), Toast.LENGTH_SHORT).show()
            }
            else {
            }
        }
    }

    private fun getAndAddOverallPoints(){
        var pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        pointsOverall++
        sharedPreferences.edit()
            .putInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, pointsOverall)
            .apply()
    }

    private fun textSize(){
        var blind = false
        binding.howToPlay.setOnClickListener{
            if (blind == false){
                binding.howToPlay.textSize = 30F
                blind = true
            }
            else
            {
                binding.howToPlay.textSize = 20F
                blind = false
            }
        }
    }

    private fun getDataFromText(): SigningEntity {
        val roza = binding.roza.text.toString()
        val tulipan = binding.tulipan.text.toString()
        val przebisnieg = binding.przebisnieg.text.toString()
        return SigningEntity(roza, tulipan, przebisnieg)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
