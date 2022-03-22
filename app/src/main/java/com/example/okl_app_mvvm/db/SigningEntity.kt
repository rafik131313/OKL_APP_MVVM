package com.example.okl_app_mvvm.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "flowers-table")
data class SigningEntity(
    val roza: String,
    val tulipan: String,
    val przebisnieg: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
