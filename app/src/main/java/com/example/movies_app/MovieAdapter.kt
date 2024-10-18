package com.example.movies_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.movies_app.model.Movie
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class MovieAdapter(
    private var moviesList: List<Movie>,
    private val onNoResultsCallback: (Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable {

    private var filteredMoviesList: List<Movie> = moviesList

    // Método para actualizar la lista de películas
    fun updateMovies(newMovies: List<Movie>) {
        moviesList = newMovies
        filteredMoviesList = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = filteredMoviesList[position]
        holder.title.text = movie.name
        holder.releaseDate.text = movie.premiered

        // Cargar la imagen con Glide
        Glide.with(holder.itemView.context)
            .load(movie.image.medium)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.poster)

        // Calcular cuántas estrellas mostrar
        val rating = movie.rating.average
        val starCount = (rating / 2).toInt() // Divide el rating por 2 si la calificación máxima es 10

        // Mostrar las estrellas
        val stars = listOf(
            holder.star1,
            holder.star2,
            holder.star3,
            holder.star4,
            holder.star5
        )

        for (i in stars.indices) {
            stars[i].visibility = if (i < starCount) View.VISIBLE else View.GONE
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MovieDetailActivity::class.java).apply {
                putExtra("MOVIE_ID", movie.id)
                putExtra("MOVIE_TITLE", movie.name)
                putExtra("MOVIE_RELEASE_DATE", movie.premiered)
                putExtra("MOVIE_RATING", movie.rating.average)
                putExtra("MOVIE_GENRES", movie.genres.joinToString(", "))
                putExtra("MOVIE_DESCRIPTION", movie.summary)
                putExtra("MOVIE_IMAGE_URL", movie.image.medium)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredMoviesList.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.movie_poster)
        val title: TextView = itemView.findViewById(R.id.movie_title)
        val releaseDate: TextView = itemView.findViewById(R.id.movie_release_date)
        val rating: TextView = itemView.findViewById(R.id.movie_rating)
        val star1: ImageView = itemView.findViewById(R.id.star_1)
        val star2: ImageView = itemView.findViewById(R.id.star_2)
        val star3: ImageView = itemView.findViewById(R.id.star_3)
        val star4: ImageView = itemView.findViewById(R.id.star_4)
        val star5: ImageView = itemView.findViewById(R.id.star_5)
    }

    // Implementación del método de filtrado
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
                filteredMoviesList = if (charString.isEmpty()) {
                    moviesList
                } else {
                    moviesList.filter {
                        it.name.lowercase(Locale.getDefault()).contains(charString)
                    }
                }

                // Notificar si no hay resultados
                onNoResultsCallback(filteredMoviesList.isEmpty())

                return FilterResults().apply { values = filteredMoviesList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMoviesList = results?.values as List<Movie>
                notifyDataSetChanged()
            }
        }
    }
}
