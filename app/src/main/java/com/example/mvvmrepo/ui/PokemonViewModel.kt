package com.example.mvvmrepo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmrepo.data.base.BaseService
import com.example.mvvmrepo.data.model.PokemonInfo
import com.example.mvvmrepo.data.remote.PokemonRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {
    val dictionaryResponseLiveData = MutableLiveData<PokemonInfo>()
    var progressLiveData = MutableLiveData<Boolean>()
    var showAlertLiveData = MutableLiveData<String>()

    private val repository = PokemonRepository(
        BaseService.getBaseApi()!!
    )

    fun searchPokemon(sf: String) {
        progressLiveData.postValue(true)
        repository.getPokemon(sf).enqueue(object : Callback<PokemonInfo> {

            override fun onFailure(call: Call<PokemonInfo>, t: Throwable) {
                progressLiveData.postValue(false)
                showAlertLiveData.postValue(t.localizedMessage.toString())
            }

            override fun onResponse(
                call: Call<PokemonInfo>,
                response: Response<PokemonInfo>
            ) {
                progressLiveData.postValue(false)
                dictionaryResponseLiveData.postValue(
                    response.body()
                )
            }
        })
    }
}
