package com.example.movies_app.data.repository

import android.util.Log
import com.example.movies_app.data.api.ApiClient
import com.example.movies_app.data.model.Movie

class MovieRepository {

    // Método para obtener todas las películas
    suspend fun getAllMovies(): List<Movie> {
        val response = ApiClient.apiService.getAllMovies()
        return if (response.isSuccessful) {
            response.body() ?: listOf()
        } else {
            Log.e("MovieRepository", "Error: ${response.code()} - ${response.message()}")
            listOf()
        }
    }

    // Método para buscar películas por nombre
    suspend fun searchMovies(query: String): List<Movie> {
        val response = ApiClient.apiService.searchMovies(query)
        return if (response.isSuccessful) {
            val searchResults = response.body() ?: listOf()
            val movies = searchResults.map { it.show } // Extrae los objetos `Movie`
            Log.d("MovieRepository", "Found ${movies.size} movies for query: $query")
            movies
        } else {
            Log.e("MovieRepository", "Error: ${response.code()} - ${response.message()}")
            listOf()
        }
    }
}
