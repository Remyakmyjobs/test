package com.example.mvvmrepo.data.remote

import com.example.mvvmrepo.data.base.PokemonAPIService
import com.example.mvvmrepo.data.model.PokemonInfo
import retrofit2.Call


class PokemonRepository(private val apiService: PokemonAPIService) {
    fun getPokemon(sf : String): Call<PokemonInfo> {
        return apiService.getPokemon(sf)
    }
}