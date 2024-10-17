package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.databinding.ItemFavoriteBinding // Asegúrate de tener este import
import com.example.movies_app.model.TvShow
import java.text.SimpleDateFormat
import java.util.*

class FavoritesAdapter(private val shows: List<TvShow>) :
 RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        // Usar el binding para inflar el diseño
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    inner class FavoritesViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: TvShow) {
            // Cargar la portada
            Glide.with(binding.root.context)
                .load(show.imageUrl)
                .into(binding.imageViewCover)

            // Configurar el título
            binding.textViewTitle.text = show.title

            // Configurar la fecha en formato "día mes año"
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            binding.textViewDate.text = dateFormat.format(show.premiereDate)
        }
    }

}
