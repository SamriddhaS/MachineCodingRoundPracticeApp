package com.example.machinecodingroundapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machinecodingroundapp.data.model.PokemonResult
import com.example.machinecodingroundapp.repository.PokemonRepository
import com.example.machinecodingroundapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonResult>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResult>> = _pokemonList.asStateFlow()

    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> = _loadError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentOffset = 0
    private val pokemonLoadLimit = 10

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            _isLoading.value = true
            _loadError.value = null
            val result = repository.getPokemonList(pokemonLoadLimit, currentOffset)
            when (result) {
                is Resource.Success -> {
                    val newList = _pokemonList.value + (result.data.results)
                    _pokemonList.value = newList
                    currentOffset += pokemonLoadLimit
                }
                is Resource.Error -> {
                    _loadError.value = result.message
                }
                is Resource.Loading -> {
                    // Not used in this flow, but could be handled if needed
                }
            }
            _isLoading.value = false
        }
    }
}
