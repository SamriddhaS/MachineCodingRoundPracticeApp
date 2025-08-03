package com.example.machinecodingroundapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machinecodingroundapp.data.models.PokemonListItem
import com.example.machinecodingroundapp.repositories.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel
@Inject constructor(
    private val repository: PokemonRepository,
) : ViewModel() {

    private var pageNo = 0
    private var loadNumber = 20

    private val _pokemonList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListItem>> = _pokemonList.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError.asStateFlow()

    init {
        loadPokemonWithPagination()
        viewModelScope.launch {
            repository.observePokemonList().collectLatest{
                _pokemonList.value = it
            }
        }
    }

    fun loadPokemonWithPagination() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.loadPokemonList(loadNumber, pageNo)
                pageNo++
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _isError.value = e.message
            }
        }
    }
}