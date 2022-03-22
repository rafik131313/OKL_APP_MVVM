package com.example.okl_app_mvvm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okl_app_mvvm.db.SigningEntity
import com.example.okl_app_mvvm.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel(){

    fun insertSigning(signingentity: SigningEntity) = viewModelScope.launch {
        mainRepository.insertSigning(signingentity)
    }

    fun updateFlowers(signingentity: SigningEntity) = viewModelScope.launch {
        mainRepository.updateSigning(signingentity)
    }


    val getAllFlowers = mainRepository.getAllFlowers()


}