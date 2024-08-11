package com.example.netflixretrofit.remote

import com.example.netflixretrofit.model.FilmeRecente
import com.example.netflixretrofit.model.FilmesResposta
import com.example.netflixretrofit.util.Const
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBAPI {

    @GET("movie/latest")
    suspend fun recurarFilmeRecente(): Response<FilmeRecente>

    @GET("movie/popular")
    suspend fun recurarFilmesPopulares(@Query("page") pagina: Int): Response<FilmesResposta>
}