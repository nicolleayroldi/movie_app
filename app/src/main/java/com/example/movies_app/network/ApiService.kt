package com.example.movies_app.network

import com.example.movies_app.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("shows") // Endpoint para obtener la lista de películas
    fun getMovies(): Call<List<Movie>>

    @GET("shows/{id}") // Endpoint para obtener detalles de una película
    fun getShowDetails(@Path("id") id: Int): Call<Movie>
}
