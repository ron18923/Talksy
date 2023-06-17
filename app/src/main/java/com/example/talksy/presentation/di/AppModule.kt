package com.example.talksy.presentation.di

import com.example.talksy.data.MainRepository
import com.example.talksy.data.helperRepositories.FireStoreRepository
import com.example.talksy.data.helperRepositories.StorageRepository
import com.example.talksy.data.helperRepositories.UserRepository
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
    fun providesUserRepository(): UserRepository {
        return UserRepository(auth = Firebase.auth)
    }

    @Provides
    @Singleton
    fun providesStorageRepository(): StorageRepository {
        return StorageRepository()
    }

    @Provides
    @Singleton
    fun providesFireStoreRepository(): FireStoreRepository {
        return FireStoreRepository()
    }

    @Provides
    @Singleton
    fun providesMainRepository(): MainRepository {
        return MainRepository(
            userRepository = providesUserRepository(),
            storageRepository = providesStorageRepository(),
            fireStoreRepository = providesFireStoreRepository()
        )
    }

}