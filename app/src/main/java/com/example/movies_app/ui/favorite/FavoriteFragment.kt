package com.example.movies_app.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.data.model.Movie
import com.google.gson.Gson

class FavoriteFragment : Fragment() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var textViewNoFavorites: TextView
    private var favoriteMovies: List<Movie> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // Inicializar RecyclerView
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        recyclerViewFavorites.layoutManager = GridLayoutManager(context, 2)

        // Inicializar el TextView para el mensaje de "No Favoritos"
        textViewNoFavorites = view.findViewById(R.id.textViewNoFavorites)

        // Cargar favoritos primero
        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        favoriteMovies = getFavoriteMovies()

        // Inicializa el adaptador aquí después de cargar los favoritos
        favoriteAdapter = FavoriteAdapter(favoriteMovies)
        recyclerViewFavorites.adapter = favoriteAdapter

        // Verifica si hay películas favoritas y actualiza la visibilidad del mensaje
        if (favoriteMovies.isEmpty()) {
            textViewNoFavorites.visibility = View.VISIBLE
            recyclerViewFavorites.visibility = View.GONE
        } else {
            textViewNoFavorites.visibility = View.GONE
            recyclerViewFavorites.visibility = View.VISIBLE
            favoriteAdapter.notifyDataSetChanged()
        }
    }

    private fun getFavoriteMovies(): List<Movie> {
        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteMoviesJson = sharedPreferences.getString("favorite_movies", "[]") ?: "[]"
        return Gson().fromJson(favoriteMoviesJson, Array<Movie>::class.java).toList()
    }

}

