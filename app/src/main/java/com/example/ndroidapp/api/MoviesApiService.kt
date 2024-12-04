package com.example.ndroidapp.api

import com.example.ndroidapp.Domain.CategoryResponse
import com.example.ndroidapp.Domain.ListFilm
import com.example.ndroidapp.Domain.MovieActor
import com.example.ndroidapp.Domain.MovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    // Получение списка фильмов
    @GET("movies")
    suspend fun getMovies(@Query("page") page: Int): Response<ListFilm>

    // Получение фильмов по категории
    @GET("categories/{categoryId}/movies")
    suspend fun getMoviesByCategory(@Path("categoryId") categoryId: Int): Response<ListFilm>

    // Получение категорий
    @GET("categories")
    suspend fun getCategories(): Response<CategoryResponse>

    // Получение жанров
    @GET("genres")
    suspend fun getGenres(): Response<CategoryResponse>

    // Получение актеров фильма
    @GET("movies/{movieId}/actors")
    suspend fun getMovieActors(@Path("movieId") movieId: Int): Response<List<MovieActor>>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): Response<MovieDetails>
}

