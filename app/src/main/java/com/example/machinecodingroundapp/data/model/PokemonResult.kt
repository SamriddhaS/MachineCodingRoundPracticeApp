package com.example.machinecodingroundapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) {
    val imageUrl: String
        get() {
            val id = url.trimEnd('/').substringAfterLast('/')
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
}
