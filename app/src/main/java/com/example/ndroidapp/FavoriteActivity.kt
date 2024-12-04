package com.example.ndroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ndroidapp.R
import com.example.ndroidapp.Domain.Datum
import com.example.ndroidapp.adapters.MovieAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteActivity : AppCompatActivity() {
    private lateinit var textViewFavorites: TextView
    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var favoriteMovies: List<Datum>
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Привязка элементов UI с помощью findViewById
        textViewFavorites = findViewById(R.id.favoriteMoviesTitle)
        recyclerViewFavorites = findViewById(R.id.favoriteMoviesRecyclerView)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Получаем список фильмов из Intent
        favoriteMovies = intent.getParcelableArrayListExtra("favorites")
            ?: emptyList() // Используем пустой список, если данных нет

        // Настроим адаптер для RecyclerView
        val adapter = MovieAdapter(favoriteMovies) { movie ->
            // Обработчик кликов по фильму, если нужно что-то сделать при клике
        }

        // Устанавливаем адаптер для RecyclerView
        recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        recyclerViewFavorites.adapter = adapter

        // Установка текста для заголовка
        textViewFavorites.text = "Favorite Movies"

        // Обработка навигации
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navigateToActivity(MainActivity::class.java)
                    true
                }

                R.id.navigation_favorites -> {
                    // Уже в экране Favorites, ничего не делаем
                    true
                }

                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }
}
