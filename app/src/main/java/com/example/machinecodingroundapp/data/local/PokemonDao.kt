package com.example.machinecodingroundapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.machinecodingroundapp.data.models.PokemonListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonListItem>)

    @Query("SELECT * FROM pokemon_list_item")
    suspend fun getPokemonList(): List<PokemonListItem>

    @Query("SELECT * FROM pokemon_list_item")
    fun observePokemonList(): Flow<List<PokemonListItem>>

}