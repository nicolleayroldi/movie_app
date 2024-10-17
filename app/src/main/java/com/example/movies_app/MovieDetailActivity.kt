package com.example.movies_app

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movies_app.model.TvShow
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var rating: TextView
    private lateinit var genres: TextView
    private lateinit var description: TextView
    private lateinit var poster: ImageView
    private lateinit var btnFavorite: ImageButton
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Inicializa las vistas
        title = findViewById(R.id.movie_title)
        releaseDate = findViewById(R.id.movie_release_date)
        rating = findViewById(R.id.movie_rating)
        genres = findViewById(R.id.movie_detail_genres)
        description = findViewById(R.id.movie_detail_summary)
        poster = findViewById(R.id.movie_poster)
        btnFavorite = findViewById(R.id.btnFavorite)

        // Recupera los datos del Intent
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        val movieTitle = intent.getStringExtra("MOVIE_TITLE")
        val movieReleaseDate = intent.getStringExtra("MOVIE_RELEASE_DATE")
        val movieRating = intent.getStringExtra("MOVIE_RATING")
        val movieGenres = intent.getStringExtra("MOVIE_GENRES")
        val movieDescription = intent.getStringExtra("MOVIE_DESCRIPTION")
        val movieImageUrl = intent.getStringExtra("MOVIE_IMAGE_URL")

        // Asigna los datos a las vistas
        title.text = movieTitle
        releaseDate.text = "Fecha de lanzamiento: $movieReleaseDate"
        rating.text = "Rating: $movieRating"

        // Procesar y formatear los géneros
        val genresList = movieGenres?.split(",")?.map { it.trim() } // Divide la cadena en una lista y elimina espacios
        val formattedGenres = genresList?.joinToString(".") ?: "" // Une la lista con puntos

        // Asigna el texto con el formato deseado
        genres.text = "Géneros: $formattedGenres"

        description.text = movieDescription

        // Cargar la imagen con Glide
        Glide.with(this)
            .load(movieImageUrl)
            .into(poster)

        // Cargar el estado desde SharedPreferences
        val sharedPreferences = getSharedPreferences("MOVIE_PREFS", MODE_PRIVATE)
        isFavorite = sharedPreferences.getBoolean("MOVIE_$movieId", false)

        // Configurar el estado inicial del botón
        updateFavoriteButton()

        // Configurar el clic del botón
        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite // Cambiar el estado de favorito
            updateFavoriteButton()

            // Guardar el estado en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putBoolean("MOVIE_$movieId", isFavorite)
            editor.apply()

            // Crear el objeto TvShow con valores seguros
            val safeTitle = movieTitle ?: "Unknown Title"
            val safeImageUrl = movieImageUrl ?: ""
            val safeReleaseDate = movieReleaseDate?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            } ?: Date()

            // Agregar o eliminar de favoritos según el estado
            if (isFavorite) {
                saveFavoriteMovie(movieId, safeTitle, safeImageUrl, safeReleaseDate)
            } else {
                removeFavoriteMovie(movieId)
            }
        }
    }


    // Guardar película en favoritos
    private fun saveFavoriteMovie(movieId: Int, title: String, imageUrl: String, releaseDate: Date) {
        val sharedPreferences = getSharedPreferences("MOVIE_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val favoriteMovies = sharedPreferences.getStringSet("FAVORITE_MOVIES", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Formato de datos: movieId|title|imageUrl|releaseDate
        val movieData = "$movieId|$title|$imageUrl|${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(releaseDate)}"
        favoriteMovies.add(movieData)

        // Guardamos la lista de películas favoritas
        editor.putStringSet("FAVORITE_MOVIES", favoriteMovies)
        editor.apply()

        println("Favorito guardado: $movieData")
    }

    // Eliminar película de favoritos
    private fun removeFavoriteMovie(movieId: Int) {
        val sharedPreferences = getSharedPreferences("MOVIE_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val favoriteMovies = sharedPreferences.getStringSet("FAVORITE_MOVIES", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Buscar y eliminar la película por ID
        favoriteMovies.removeIf { it.startsWith("$movieId|") }

        editor.putStringSet("FAVORITE_MOVIES", favoriteMovies)
        editor.apply()
    }


    private fun updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.favorite)
        } else {
            btnFavorite.setImageResource(R.drawable.favorite_border)
        }
    }

    private fun getFavoriteShows(): List<TvShow> {
        val sharedPreferences = getSharedPreferences("MOVIE_PREFS", Context.MODE_PRIVATE)
        val favoriteMovies = sharedPreferences?.getStringSet("FAVORITE_MOVIES", mutableSetOf())?.toList() ?: emptyList()

        // Agrega un log para verificar qué datos estás obteniendo
        println("Películas favoritas recuperadas: $favoriteMovies")

        // Mapear los datos de cadena a objetos TvShow
        return favoriteMovies.map { movieData ->
            val movieParts = movieData.split("|")
            val movieId = movieParts[0].toInt()
            val title = movieParts[1]
            val imageUrl = movieParts[2]
            val releaseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movieParts[3])
            TvShow(movieId, title, "", releaseDate, imageUrl)
        }
    }


}
