package com.example.okl_app_mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SigningEntity::class],
    version = 1
)

abstract class SigningDatabase: RoomDatabase() {

    abstract fun getSigningDAO(): SigningDAO


}