package com.example.machinecodingroundapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("pokemon_list_item")
data class PokemonListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("name")
    val name: String,
)
