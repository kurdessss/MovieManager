package com.example.ndroidapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ndroidapp.Domain.MovieActor
import com.example.ndroidapp.R

data class Actor(
    val name: String,
    val imageUrl: String // URL изображения актера
)

class ActorAdapter(private val context: Context, private var actors: List<MovieActor>) : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    // ViewHolder для элемента списка
    class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val actorImage: ImageView = view.findViewById(R.id.ActorImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewholder_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]

        // Загружаем изображение актера с помощью Glide
        Glide.with(context)
            .load(actor.imageUrl) // предполагается, что в модели MovieActor есть свойство imageUrl
            .placeholder(R.drawable.placeholder_image)
            .into(holder.actorImage)
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    // Метод для обновления списка актеров
    fun setActors(newActors: List<MovieActor>) {
        actors = newActors
        notifyDataSetChanged() // Обновляем адаптер
    }
}