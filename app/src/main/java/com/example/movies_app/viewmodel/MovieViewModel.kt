package com.example.movies_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.data.model.Movie
import com.example.movies_app.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    init {
        // Cargar todas las películas al inicializar el ViewModel
        getAllMovies()
    }

    fun getAllMovies() {
        viewModelScope.launch {
            // Obtiene todas las películas de forma suspendida
            _movies.value = movieRepository.getAllMovies()
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _movies.value = movieRepository.searchMovies(query)
            }
        }
}
