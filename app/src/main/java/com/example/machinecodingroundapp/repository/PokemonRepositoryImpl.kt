package com.example.machinecodingroundapp.repository

import com.example.machinecodingroundapp.data.model.PokemonListResponse
import com.example.machinecodingroundapp.data.remote.PokemonApiService
import com.example.machinecodingroundapp.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApiService
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse> {
        return try {
            val response = apiService.getPokemonList(limit, offset)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection.")
        } catch (e: HttpException) {
            Resource.Error("An unexpected error occurred: ${e.code()} ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Unknown error: ${e.localizedMessage ?: "No message"}")
        }
    }
}
