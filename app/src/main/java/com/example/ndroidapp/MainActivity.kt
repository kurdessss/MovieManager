package com.example.ndroidapp

import com.example.ndroidapp.DetailActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.ndroidapp.Domain.Category
import com.example.ndroidapp.Domain.SliderItems
import com.example.ndroidapp.adapters.CategoryListAdapter
import com.example.ndroidapp.adapters.MovieAdapter
import com.example.ndroidapp.adapters.SliderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ndroidapp.Domain.FilmItem
import com.example.ndroidapp.api.ApiClient
import com.example.ndroidapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bestMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var categoryAdapter: CategoryListAdapter

    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mStringRequest: StringRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверка на авторизацию пользователя
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Инициализация элементов
        setupSlider()
        setupBestMoviesRecyclerView()
        setupUpcomingMoviesRecyclerView()
        setupCategoryRecyclerView()

        // Загрузка данных
        fetchBestMovies(1)
        fetchUpcomingMovies(2)
        sendRequestCategory()

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Проверяем, если активность уже в стеке, то не создаем новую
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorites -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun sendRequest() {
        // URL для запроса всех фильмов
        val url = "https://moviesapi.ir/api/v1/movies"

        // Выполняем запрос с использованием Volley
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val gson = Gson()
                val movies: List<FilmItem> = gson.fromJson(response, Array<FilmItem>::class.java).toList()

                // Генерация ссылки на видео для каждого фильма
                val baseVideoUrl = "http://192.168.1.2:8000/" // Базовый URL для видео
                movies.forEach { movie ->
                    movie.videoUrl = "$baseVideoUrl${movie.id}.mp4" // Ссылка на видео
                }

            },
            { error ->
                Toast.makeText(this, "Error fetching movies.", Toast.LENGTH_SHORT).show()
            })

        mRequestQueue!!.add(stringRequest)
    }


    private fun setupSlider() {
        // Инициализация списка изображений для слайдера
        val sliderItems = mutableListOf(
            SliderItems(R.drawable.wide),
            SliderItems(R.drawable.wide1),
            SliderItems(R.drawable.wide3)
        )

        // Инициализация адаптера
        val sliderAdapter = SliderAdapter(sliderItems, binding.viewPager2)
        binding.viewPager2.adapter = sliderAdapter

        // Настройка ViewPager2
        binding.viewPager2.apply {
            clipToPadding = false  // Отключаем обрезку до краев
            clipChildren = false   // Отключаем обрезку дочерних элементов
            offscreenPageLimit = 3 // Устанавливаем количество страниц для предварительной загрузки
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER // Убираем эффект прокрутки
        }

        // CompositePageTransformer для добавления эффектов
        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40)) // Отступ между страницами
            addTransformer { page, position ->
                val r = 1 - Math.abs(position) // Плавное изменение масштаба
                page.scaleY = 0.85f + r * 0.15f // Масштабирование страниц
                page.alpha = 0.5f + r * 0.5f // Прозрачность страниц
            }
        }
        binding.viewPager2.setPageTransformer(compositePageTransformer)

        // Установка начальной позиции
        binding.viewPager2.setCurrentItem(0, false)

        // Автоматическая прокрутка
        val slideRunnable = Runnable {
            val currentItem = binding.viewPager2.currentItem
            val nextItem = if (currentItem == sliderItems.size - 1) 0 else currentItem + 1
            binding.viewPager2.setCurrentItem(nextItem, true)
        }

        // Handler для управления автоматической прокруткой
        val handler = Handler(Looper.getMainLooper())
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(slideRunnable)
                handler.postDelayed(slideRunnable, 3000) // Задержка 3 секунды
            }
        })
        handler.postDelayed(slideRunnable, 3000) // Запуск слайдера
    }



    private fun setupBestMoviesRecyclerView() {
        bestMoviesAdapter = MovieAdapter(emptyList()) { film ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("film_id", film.id)
            startActivity(intent)
        }
        binding.view1.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestMoviesAdapter
        }
    }

    private fun setupUpcomingMoviesRecyclerView() {
        upcomingMoviesAdapter = MovieAdapter(emptyList()) { film ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("film_id", film.id)
            startActivity(intent)
        }
        binding.view3.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingMoviesAdapter
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryListAdapter(emptyList()) { category ->
            fetchMoviesByCategory(category.id)
        }
        binding.view2.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun fetchBestMovies(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bestMovies = ApiClient.getBestMovies(page)
                withContext(Dispatchers.Main) {
                    Log.d("MainActivity", "Fetched best movies: ${bestMovies.size}")
                    bestMoviesAdapter.submitList(bestMovies)
                    // Скрыть прогресс-бар, когда данные загружены
                    binding.progressBar1.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching best movies: ${e.message}")
                // Скрыть прогресс-бар при ошибке
                withContext(Dispatchers.Main) {
                    binding.progressBar1.visibility = View.GONE
                }
            }
        }
    }

    private fun fetchUpcomingMovies(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val upcomingMovies = ApiClient.getUpComingMovies(page)
                withContext(Dispatchers.Main) {
                    Log.d("MainActivity", "Fetched upcoming movies: ${upcomingMovies.size}")
                    upcomingMoviesAdapter.submitList(upcomingMovies)
                    // Скрыть прогресс-бар, когда данные загружены
                    binding.progressBar3.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching upcoming movies: ${e.message}")
                // Скрыть прогресс-бар при ошибке
                withContext(Dispatchers.Main) {
                    binding.progressBar3.visibility = View.GONE
                }
            }
        }
    }

    // Заменил fetchCategories на запрос через Volley
    private fun sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(this)
        val url = "https://moviesapi.ir/api/v1/genres"

        // Показываем индикатор загрузки
        binding.progressBar2.visibility = View.VISIBLE

        mStringRequest = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            binding.progressBar2.visibility = View.GONE  // Скрыть индикатор загрузки

            // Парсим ответ JSON в список категорий
            val gson = Gson()
            val catList = gson.fromJson<List<Category>>(response, object : TypeToken<List<Category>>() {}.type)  // Исправлено

            // Обновляем адаптер с полученными категориями
            categoryAdapter.submitList(catList)
        }, Response.ErrorListener { error ->
            binding.progressBar2.visibility = View.GONE  // Скрыть индикатор загрузки
            Log.e("MainActivity", "Error fetching categories: ${error.toString()}")
        })

        // Добавляем запрос в очередь
        mRequestQueue.add(mStringRequest)
    }

    private fun fetchMoviesByCategory(categoryId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val moviesByCategory = ApiClient.getMoviesByCategory(categoryId)
                withContext(Dispatchers.Main) {
                    Log.d("MainActivity", "Fetched movies for category $categoryId: ${moviesByCategory.size}")
                    bestMoviesAdapter.submitList(moviesByCategory)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching movies by category: $e")  // Исправлено
            }
        }
    }
}
