package com.example.movieappmvvm.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieappmvvm.R
import com.example.movieappmvvm.data.api.POSTER_BASE_URL
import com.example.movieappmvvm.data.api.TheMovieDBClient
import com.example.movieappmvvm.data.api.TheMovieDBInterface
import com.example.movieappmvvm.data.model.MovieDetails
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.databinding.ActivityMainBinding
import com.example.movieappmvvm.databinding.ActivitySingleMovieBinding
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var binding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId: Int = intent.getIntExtra("id",1)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetails) {
        binding.tvMovieTitle.text = it.title
        binding.tvMovieTagline.text = it.tagline
        binding.tvReleaseDate.text = it.releaseDate
        binding.tvRating.text = it.rating.toString()
        binding.tvRuntime.text = it.runtime.toString() + " minutes"
        binding.tvOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.tvBudget.text = formatCurrency.format(it.budget)
        binding.tvRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this).load(moviePosterURL).into(binding.imvMoviePoster)
    }

    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}