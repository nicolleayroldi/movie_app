package com.example.movies_app.ui.movie

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.data.model.Movie
import com.example.movies_app.data.repository.MovieRepository
import com.example.movies_app.viewmodel.MovieViewModel
import com.example.movies_app.viewmodel.MovieViewModelFactory

class MovieFragment : Fragment(), MovieAdapter.OnMovieClickListener { // Implementar el listener
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var searchView: SearchView
    private lateinit var noResultsText: TextView // TextView para mostrar mensaje sin resultados

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_movies, container, false)

        // Configurar RecyclerView y Adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_movies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        movieAdapter = MovieAdapter(listOf(), this) // Pasar el listener al adapter
        recyclerView.adapter = movieAdapter

        // Configurar TextView para mostrar mensaje sin resultados
        noResultsText = view.findViewById(R.id.text_view_no_results) // Asegúrate de que el ID coincida con el layout

        // Inicializar el repositorio
        val movieRepository = MovieRepository()

        // Inicializar el ViewModel con la Factory
        val factory = MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        // Observar los cambios en la lista de películas
        movieViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isEmpty()) {
                // Si no hay resultados, muestra el mensaje de "sin resultados"
                val searchTerm = searchView.query.toString()
                noResultsText.text = getString(R.string.no_results_found, searchTerm)
                noResultsText.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                // Si hay resultados, oculta el mensaje y muestra el RecyclerView
                noResultsText.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                movieAdapter.updateMovies(movies)
            }
        }

        // Cargar todas las películas al iniciar el fragmento
        movieViewModel.getAllMovies()

        // Configurar SearchView
        searchView = view.findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No se necesita hacer nada aquí
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    movieViewModel.getAllMovies()
                } else {
                    movieViewModel.searchMovies(newText)
                }
                return true
            }
        })

        return view
    }

    // Manejar el clic en la película
    override fun onMovieClick(movie: Movie) {
        // Navegar al MovieDetailFragment
        val fragment = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("movie", movie)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

