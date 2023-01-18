package com.example.movieappmvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieappmvvm.data.model.MovieDetails
import com.example.movieappmvvm.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int): ViewModel(){
    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy{
        movieRepository.fetchSingMovieDetails(compositeDisposable,movieId)
    }
    val networkState: LiveData<NetworkState> by lazy{
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared(){
        super.onCleared()
        compositeDisposable.dispose()
    }
}