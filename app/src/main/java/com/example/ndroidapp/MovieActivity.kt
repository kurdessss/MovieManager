/*
package com.example.ndroidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
*/
/*import com.example.ndroidapp.fragments.FavoritesFragment
import com.example.ndroidapp.fragments.HomeFragment
import com.example.ndroidapp.fragments.ProfileFragment
import com.example.ndroidapp.fragments.SearchFragment
import com.example.ndroidapp.fragments.SettingsFragment*//*

import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomnavigation.BottomNavigationView

class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Устанавливаем обработчик нажатий для нижней панели навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Переход на главный экран
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment())
                        .commit()
                    true
                }
                R.id.navigation_favorites -> {
                    // Переход на экран "Избранное"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FavoritesFragment())
                        .commit()
                    true
                }
                R.id.navigation_search -> {
                    // Переход на экран "Поиск"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchFragment())
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    // Переход на экран "Настройки"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}*/
