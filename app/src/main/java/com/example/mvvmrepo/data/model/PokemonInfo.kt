package com.example.mvvmrepo.data.model

data class PokemonInfo(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int = 0,
    val types: List<TypeResponse>,
) {

    fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
    fun getPrice(): String = "$" + try {
        getPriceInt()
    } catch (e: Exception) {
        "0.0"
    }

    fun getPriceInt(): Int = try {
        (base_experience / 100) * 6
    } catch (e: Exception) {
        0
    }

    fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)

    fun getImageUrl(): String {
        try {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        } catch (e: Exception) {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
        }
    }

    data class TypeResponse(
        val slot: Int,
        val type: Type
    )

    data class Type(
        val name: String
    )
}