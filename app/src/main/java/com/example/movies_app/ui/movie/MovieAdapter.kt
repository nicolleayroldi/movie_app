package com.example.movies_app.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.data.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movies: List<Movie>,
    private val listener: OnMovieClickListener // Agregar el listener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    fun updateMovies(newMovies: List<Movie>) {
        // Comparar las nuevas películas con las existentes y realizar actualizaciones específicas
        val oldMoviesCount = movies.size
        movies = newMovies
        notifyItemRangeChanged(0, oldMoviesCount) // Actualiza los elementos existentes

        // Si la nueva lista tiene más elementos, notificar la inserción
        if (newMovies.size > oldMoviesCount) {
            notifyItemRangeInserted(oldMoviesCount, newMovies.size - oldMoviesCount)
        }
        // Si la nueva lista tiene menos elementos, notificar la eliminación
        else if (newMovies.size < oldMoviesCount) {
            notifyItemRangeRemoved(newMovies.size, oldMoviesCount - newMovies.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], listener) // Pasar el listener
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        private val releaseDate: TextView = itemView.findViewById(R.id.movie_release_date)
        private val poster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val movieRatingStars: LinearLayout = itemView.findViewById(R.id.movie_rating_stars)

        fun bind(movie: Movie, listener: OnMovieClickListener) {
            title.text = movie.name
            releaseDate.text = movie.premiered

            // Cargar la imagen usando Picasso
            Picasso.get()
                .load(movie.image?.original)
                .placeholder(R.drawable.placeholder_image)
                .into(poster)

            itemView.setOnClickListener { listener.onMovieClick(movie) } // Manejar el clic

            // Obtener el rating promedio
            val ratingValue = movie.rating?.average?.div(2)?.toInt() ?: 0 // Dividir el rating entre 2, o 0 si es nulo
            for (i in 0 until 5) {
                val star = movieRatingStars.getChildAt(i) as ImageView
                if (i < ratingValue) {
                    star.visibility = View.VISIBLE // Mostrar la estrella
                } else {
                    star.visibility = View.INVISIBLE // Ocultar la estrella
                }
            }
        }
    }
}
