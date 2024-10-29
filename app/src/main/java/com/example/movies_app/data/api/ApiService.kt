package com.example.movies_app.data.api

import com.example.movies_app.data.model.Movie
import com.example.movies_app.data.model.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("shows") // Endpoint para obtener todas las películas
    suspend fun getAllMovies(): Response<List<Movie>>

    @GET("search/shows") // Endpoint para búsqueda de películas
    suspend fun searchMovies(@Query("q") query: String): Response<List<SearchResult>>
}
