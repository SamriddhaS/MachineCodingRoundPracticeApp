package com.example.machinecodingroundapp.di

import com.example.machinecodingroundapp.data.remote.PokemonApiService
import com.example.machinecodingroundapp.repositories.PokemonRepository
import com.example.machinecodingroundapp.repositories.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesPokemonApiService(retrofit: Retrofit): PokemonApiService {
        return retrofit.create(PokemonApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesPokemonRepository(pokemonApiService: PokemonApiService): PokemonRepository {
        return PokemonRepositoryImpl(pokemonApiService)
    }
}