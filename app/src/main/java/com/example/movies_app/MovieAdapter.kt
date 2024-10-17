package com.example.movies_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.model.Movie
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale
import android.content.Intent

class MovieAdapter(
    private var moviesList: List<Movie>,
    private val onNoResultsCallback: (Boolean) -> Unit // Agregar callback como segundo parámetro
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable {

    private var filteredMoviesList: List<Movie> = moviesList

    // Método para actualizar la lista de películas
    fun updateMovies(newMovies: List<Movie>) {
        moviesList = newMovies
        filteredMoviesList = newMovies // Asegúrate de actualizar también la lista filtrada
        notifyDataSetChanged() // Refresca la lista en el RecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = filteredMoviesList[position]
        holder.title.text = movie.name
        holder.releaseDate.text = movie.premiered
        holder.rating.text = "Rating: ${movie.rating.average}"

        // Cargar la imagen con Glide
        Glide.with(holder.itemView.context)
            .load(movie.image.medium)
            .into(holder.poster)

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

                val filterResults = FilterResults()
                filterResults.values = filteredMoviesList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMoviesList = if (results?.values != null) {
                    results.values as List<Movie>
                } else {
                    emptyList()
                }
                notifyDataSetChanged()
            }

        }
    }
}
