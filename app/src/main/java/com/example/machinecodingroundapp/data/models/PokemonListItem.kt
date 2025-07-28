package com.example.machinecodingroundapp.data.models

import com.google.gson.annotations.SerializedName

data class PokemonListItem(
    @SerializedName("name")
    val name: String,
)
