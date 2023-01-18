package com.example.movieappmvvm.data.model


import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("original_language")
    val language: String,
    @SerializedName("vote_count")
    val vote: String,
    val popularity: Float,
)