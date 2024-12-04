package com.example.ndroidapp.Domain

data class MovieDetails(
    val id: Int,
    val title: String,
    val poster: String, // Это URL постера фильма
    val year: String,
    val rated: String,
    val released: String,
    val runtime: String,
    val director: String,
    val writer: String,
    val actors: List<MovieActor>, // Изменено на List<MovieActor> для списка актеров
    val plot: String, // Изменено с summary на plot
    val country: String,
    val awards: String,
    val metascore: String,
    val imdb_rating: String,
    val imdb_votes: String,
    val imdb_id: String,
    val type: String,
    val genres: List<String>,
    val images: List<String>
)