package com.example.mvvmrepo.ui

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmrepo.R
import com.example.mvvmrepo.databinding.ActivityMainBinding
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PokemonViewModel
    private var balance = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViewModel()
        initViews()
        initObservers()
    }

    private fun initObservers() {
        viewModel.dictionaryResponseLiveData.observe(this, Observer {
            if (it == null) {
                Toast.makeText(this@MainActivity, "No data found.", Toast.LENGTH_SHORT)
                    .show()
                return@Observer
            }
            binding.constraintData.visibility = View.VISIBLE
            Log.e("!_@_@", it.name + "--")
            binding.pokemon = it
            var name: List<String>
            try {
                name = emptyList<String>()
                it.types.forEach { resp ->
                    name += resp.type.name
                }
                binding.statsTitle.text = name.joinToString(",")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            balance = Random().nextInt(20)
            binding.balance.text = "Your balance is : $$balance"
            setImages(it.getImageUrl())
        })

        viewModel.progressLiveData.observe(this, Observer {
            if (it) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        })

        viewModel.showAlertLiveData.observe(this, Observer {
            if (it != null && !it.isEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setImages(imageUrl: String) {
        try {
            Glide.with(this@MainActivity)
                .load(imageUrl)
                .listener(
                    GlidePalette.with(imageUrl)
                        .use(BitmapPalette.Profile.MUTED_LIGHT)
                        .intoCallBack { palette ->
                            val rgb = palette?.dominantSwatch?.rgb
                            if (rgb != null) {
                                try {
                                    binding.header.setCardBackgroundColor(rgb)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.crossfade(true)
                ).into(binding.image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        binding.buyPokeMin.setOnClickListener {
            Toast.makeText(
                this@MainActivity, if (balance < binding.pokemon?.getPriceInt() ?: 0) {
                    "Insufficient balance"
                } else {
                    "Congrats!! you can buy this pokemon"
                }, Toast.LENGTH_LONG
            ).show()
        }

        binding.btnSearch.setOnClickListener {
            var searchText = binding.edtSearch.text.toString()
            if (TextUtils.isEmpty(searchText)) {
                Toast.makeText(this@MainActivity, "Please enter pokemon name", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.searchPokemon(searchText)
                binding.constraintData.visibility = View.GONE
                hideKeybaord(it)
            }
        }
    }

    private fun hideKeybaord(view: View) {
        try {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViewModel() {
        //initialize viewModel
        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
    }
}