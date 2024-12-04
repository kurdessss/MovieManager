package com.example.ndroidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.ndroidapp.Domain.Datum
import com.example.ndroidapp.R

class MovieAdapter(private var movies: List<Datum>, private val onItemClick: (Datum) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    fun submitList(newMovies: List<Datum>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount() = movies.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.pic)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTxt)

        fun bind(movie: Datum) {
            titleTextView.text = movie.title

            // Используем Glide с RoundedCorners для округления углов изображения
            Glide.with(itemView.context)
                .load(movie.poster)
                .placeholder(R.drawable.placeholder_image) // замените на ваш placeholder
                .error(R.drawable.error_image) // замените на изображение ошибки
                .transform(RoundedCorners(16)) // Указываем радиус округления (16dp)
                .into(imageView)
        }
    }
}