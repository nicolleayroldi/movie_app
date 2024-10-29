package com.example.movies_app.ui.movie

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movies_app.R
import com.example.movies_app.data.model.Movie
import com.squareup.picasso.Picasso
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson

class MovieDetailFragment : Fragment() {

    private lateinit var movie: Movie
    private var isFavorite: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.movie_detail, container, false)

        // Obtener el objeto Movie pasado a través de argumentos
        movie = arguments?.getParcelable<Movie>("movie") ?: throw IllegalArgumentException("Movie is missing")


        // Configurar detalles de la película
        view.findViewById<TextView>(R.id.movie_title).text = movie.name
        view.findViewById<TextView>(R.id.movie_release_date).text = movie.premiered

        // Configurar géneros
        val genresTextView = view.findViewById<TextView>(R.id.movie_detail_genres)
        val genresString = SpannableStringBuilder()
            .append("Géneros: ", StyleSpan(android.graphics.Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(movie.genres.joinToString(", "))
        genresTextView.text = genresString

        // Configurar resumen
        val summaryTextView = view.findViewById<TextView>(R.id.movie_detail_summary)
        val summaryString = SpannableStringBuilder()
            .append("Resumen: ", StyleSpan(android.graphics.Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(movie.summary ?: "Sin resumen disponible")
        summaryTextView.text = summaryString

        // Cargar la imagen del póster
        val moviePoster = view.findViewById<ImageView>(R.id.movie_poster)
        Picasso.get().load(movie.image?.original).into(moviePoster)

        // Manejar el botón de favorito
        val favoriteButton = view.findViewById<ImageButton>(R.id.btnFavorite)

        // Comprobar y establecer el estado inicial de favorito
        isFavorite = isMovieFavorite(requireContext(), movie.id)
        updateFavoriteIcon(favoriteButton)

        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon(favoriteButton)
            saveFavoriteState(requireContext(), movie, isFavorite)
        }

        return view
    }


    private fun updateFavoriteIcon(button: ImageButton) {
        button.setImageResource(if (isFavorite) R.drawable.favorite else R.drawable.favorite_border) // Actualizar ícono según el estado
    }

    private fun saveFavoriteState(context: Context, movie: Movie, isFavorite: Boolean) {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Almacenar película en JSON
        if (isFavorite) {
            val favoriteMoviesJson = sharedPreferences.getString("favorite_movies", "[]")
            val favoriteMovies = Gson().fromJson(favoriteMoviesJson, Array<Movie>::class.java).toMutableList()
            favoriteMovies.add(movie)
            editor.putString("favorite_movies", Gson().toJson(favoriteMovies))
        } else {
            // Si se quita de favoritos, elimina la película
            val favoriteMoviesJson = sharedPreferences.getString("favorite_movies", "[]")
            val favoriteMovies = Gson().fromJson(favoriteMoviesJson, Array<Movie>::class.java).toMutableList()
            favoriteMovies.removeIf { it.id == movie.id }
            editor.putString("favorite_movies", Gson().toJson(favoriteMovies))
        }

        editor.apply()
    }



    private fun isMovieFavorite(context: Context, movieId: Int): Boolean {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteMoviesJson = sharedPreferences.getString("favorite_movies", "[]")
        val favoriteMovies = Gson().fromJson(favoriteMoviesJson, Array<Movie>::class.java).toList()

        return favoriteMovies.any { it.id == movieId } // Devuelve true si la película está en favoritos
    }

}
