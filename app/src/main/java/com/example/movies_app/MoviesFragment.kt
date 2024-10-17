package com.example.movies_app

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.movies_app.model.Movie
import com.example.movies_app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment : Fragment() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var textViewNoResults: TextView
    private lateinit var moviesList: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout correcto para el fragment
        val view = inflater.inflate(R.layout.fragment_movies, container, false)

        // Inicializar el RecyclerView, SearchView y TextView
        recyclerView = view.findViewById(R.id.recycler_view_movies)
        searchView = view.findViewById(R.id.search_view)
        textViewNoResults = view.findViewById(R.id.text_view_no_results)

        // Configurar el color de fondo desde el inicio
        searchView.setBackgroundColor(resources.getColor(R.color.colorGray, null))

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapter(emptyList()) { isEmpty ->
            handleNoResults(isEmpty)
        }
        recyclerView.adapter = movieAdapter

        // Cargar películas desde la API
        loadMoviesFromApi()

        // Configurar filtro del SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter.filter.filter(newText)
                return false
            }
        })

        return view
    }

    private fun handleNoResults(isEmpty: Boolean) {
        if (isEmpty) {
            textViewNoResults.text = "No se encontraron resultados para: ${searchView.query}"
            textViewNoResults.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            textViewNoResults.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadMoviesFromApi() {
        val call = RetrofitClient.instance.getMovies()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    moviesList = response.body() ?: emptyList()
                    movieAdapter.updateMovies(moviesList)
                    handleNoResults(moviesList.isEmpty())  // Manejar resultados vacíos
                } else {
                    showError("Error loading movies")
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                showError("Network error: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        textViewNoResults.text = message
        textViewNoResults.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}
