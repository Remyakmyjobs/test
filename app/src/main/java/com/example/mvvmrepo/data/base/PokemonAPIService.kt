package com.example.mvvmrepo.data.base

import com.example.mvvmrepo.data.model.PokemonInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonAPIService {
    @GET("pokemon/{pokemonName}")
    fun getPokemon(@Path("pokemonName") pokemonName: String): Call<PokemonInfo>
}
