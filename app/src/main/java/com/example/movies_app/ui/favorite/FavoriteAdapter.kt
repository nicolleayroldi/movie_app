package com.example.movies_app.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.data.model.Movie
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val favorites: List<Movie>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val moviePoster: ImageView = itemView.findViewById(R.id.imageViewCover)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = favorites[position]
        holder.movieTitle.text = movie.name
        holder.movieReleaseDate.text = movie.premiered
        Picasso.get().load(movie.image?.original).into(holder.moviePoster)
    }

    override fun getItemCount(): Int = favorites.size
}
