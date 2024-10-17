package com.example.movies_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.adapters.FavoritesAdapter
import com.example.movies_app.model.TvShow
import java.text.SimpleDateFormat
import java.util.*

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoFavorites: TextView
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewFavorites)
        textViewNoFavorites = view.findViewById(R.id.textViewNoFavorites)

        val favoriteShows = getFavoriteShows()

        // Si no hay favoritos, mostrar el mensaje
        if (favoriteShows.isEmpty()) {
            textViewNoFavorites.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            textViewNoFavorites.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            // Configurar el layout manager y adaptador
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            favoritesAdapter = FavoritesAdapter(favoriteShows)
            recyclerView.adapter = favoritesAdapter
        }

        return view
    }

    private fun getFavoriteShows(): List<TvShow> {
        val sharedPreferences = requireContext().getSharedPreferences("MOVIE_PREFS", Context.MODE_PRIVATE)
        val favoriteMovies = sharedPreferences.getStringSet("FAVORITE_MOVIES", mutableSetOf()) ?: return emptyList()

        // Convertir la lista de strings almacenados en objetos TvShow
        return favoriteMovies.mapNotNull { movieData ->
            val movieDetails = movieData.split("|")
            if (movieDetails.size == 4) {  // Asegurarse de que hay 4 elementos
                val id = movieDetails[0].toInt()
                val title = movieDetails[1]
                val imageUrl = movieDetails[2]
                val releaseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movieDetails[3])

                TvShow(id, title, "", releaseDate ?: Date(), imageUrl)
            } else {
                null
            }
        }
    }
}
