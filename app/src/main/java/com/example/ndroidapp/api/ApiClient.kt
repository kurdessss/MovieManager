package com.example.ndroidapp.api

import android.util.Log
import com.example.ndroidapp.api.MoviesApiService
import com.example.ndroidapp.Domain.Category
import com.example.ndroidapp.Domain.CategoryResponse
import com.example.ndroidapp.Domain.Datum
import com.example.ndroidapp.Domain.ListFilm
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://moviesapi.ir/api/v1/"
    private var retrofit: Retrofit? = null

    private val apiService: MoviesApiService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(MoviesApiService::class.java)
        }

    // Метод для получения списка фильмов
    suspend fun getBestMovies(page: Int): List<Datum> {
        val response: Response<ListFilm> = apiService.getMovies(page)
        if (response.isSuccessful) {
            val data = response.body()?.data ?: emptyList()
            Log.d("ApiClient", "Fetched movies: ${data.size}")
            return data
        } else {
            Log.e("ApiClient", "Error fetching movies: ${response.message()} | Code: ${response.code()}")
            throw Exception("Error fetching movies: ${response.message()}")
        }
    }

    // Метод для получения скорых фильмов
    suspend fun getUpComingMovies(page: Int): List<Datum> {
        val response: Response<ListFilm> = apiService.getMovies(page)
        return if (response.isSuccessful) {
            response.body()?.data ?: emptyList()
        } else {
            throw Exception("Error fetching movies: ${response.message()}")
        }
    }

    // Метод для получения категорий
    suspend fun getCategories(): List<Category> {
        val response: Response<CategoryResponse> = apiService.getCategories()
        return if (response.isSuccessful) {
            response.body()?.categories ?: emptyList()
        } else {
            throw Exception("Error fetching categories: ${response.message()}")
        }
    }

    suspend fun getMoviesByCategory(categoryId: Int): List<Datum> {
        val response: Response<ListFilm> = apiService.getMoviesByCategory(categoryId)
        return if (response.isSuccessful) {
            response.body()?.data ?: emptyList()
        } else {
            throw Exception("Error fetching movies by category: ${response.message()}")
        }
    }
}
