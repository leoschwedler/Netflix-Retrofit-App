package com.example.netflixretrofit.model

import com.google.gson.annotations.SerializedName

data class FilmesResposta(
    val page: Int,
    @SerializedName("results")
    val filmes: List<Filme>,
    val total_pages: Int,
    val total_results: Int
)