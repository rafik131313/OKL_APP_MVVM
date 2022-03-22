package com.example.okl_app_mvvm.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.okl_app_mvvm.db.SigningDatabase
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_ANNEXE
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_BISONS
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_CASTLE
import com.example.okl_app_mvvm.other.Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS
import com.example.okl_app_mvvm.other.Constants.SHARED_PREFERENCES_NAME
import com.example.okl_app_mvvm.other.Constants.SIGNING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSigningData(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SigningDatabase::class.java,
        SIGNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideSigningDao(db: SigningDatabase) = db.getSigningDAO()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPrefCastle(sharedPreferences: SharedPreferences) = sharedPreferences
        .getBoolean(KEY_SHAREDPREFERENCES_VISITING_CASTLE, false)

    @Singleton
    @Provides
    fun provideSharedPrefOverallPoints(sharedPreferences: SharedPreferences) = sharedPreferences
        .getInt(KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)

}