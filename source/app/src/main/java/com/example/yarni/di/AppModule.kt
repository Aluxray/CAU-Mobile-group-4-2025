package com.example.yarni.di

import com.example.yarni.data.firebase.PatternFirebaseDataSource
import com.example.yarni.data.repository.PatternRepositoryImpl
import com.example.yarni.domain.repository.PatternRepository
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
    fun providePatternFirebaseDataSource(): PatternFirebaseDataSource {
        return PatternFirebaseDataSource()
    }

    @Provides
    @Singleton
    fun providePatternRepository(dataSource: PatternFirebaseDataSource): PatternRepository {
        return PatternRepositoryImpl(dataSource)
    }
}
