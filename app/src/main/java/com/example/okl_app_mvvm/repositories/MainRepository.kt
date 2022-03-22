package com.example.okl_app_mvvm.repositories

import com.example.okl_app_mvvm.db.SigningDAO
import com.example.okl_app_mvvm.db.SigningEntity
import javax.inject.Inject

class MainRepository @Inject constructor(
    val signingDAO: SigningDAO
) {
        suspend fun insertSigning(signingEntity: SigningEntity) = signingDAO.insert(signingEntity)

        suspend fun updateSigning(signingEntity: SigningEntity) = signingDAO.update(signingEntity)

        fun getAllFlowers() = signingDAO.fetchAllFlowers()
}