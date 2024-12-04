/*
package com.example.ndroidapp.adapters.FavoriteAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ndroidapp.Domain.FilmItem
import com.example.ndroidapp.databinding.ItemFavoriteMovieBinding

class FavoritesAdapter(private val favoriteMovies: List<FilmItem>) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val film = favoriteMovies[position]
        holder.bind(film)
    }

    override fun getItemCount(): Int = favoriteMovies.size

    class FavoriteViewHolder(private val binding: ItemFavoriteMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: FilmItem) {
            binding.movieTitle.text = film.title
            binding.movieRating.text = film.imdbRating

            // Используем Glide для загрузки изображения
            Glide.with(binding.root.context)
                .load(film.poster) // Путь к изображению
                .into(binding.moviePoster)
        }
    }
}
*/
