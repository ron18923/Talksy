package com.example.talksy.di

import android.app.Application
import com.example.talksy.data.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(app: Application): UserRepository {
        return UserRepository(auth = Firebase.auth, appContext = app)
    }

}