package com.example.ndroidapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import android.widget.Toast
import com.example.ndroidapp.Domain.FilmItem

class DetailActivity : AppCompatActivity() {
    private var idFilm = 0
    private var progressBar: ProgressBar? = null
    private var scrollView: ScrollView? = null
    private var titleTxt: TextView? = null
    private var movieRateTxt: TextView? = null
    private var movieTimeTxt: TextView? = null
    private var movieSummaryInfo: TextView? = null
    private var movieActorsInfo: TextView? = null
    private var pic2: ImageView? = null
    private var mRequestQueue: RequestQueue? = null
    private var favoriteIcon: ImageView? = null
    private val favoriteMovies = mutableListOf<FilmItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deteil_activity)

        // Получаем idFilm из переданных данных (Intent)
        idFilm = intent.getIntExtra("film_id", 0)
        initView()  // Инициализация UI элементов
        sendRequest() // Отправка запроса на сервер
    }

    private fun initView() {
        // Инициализация всех UI элементов
        progressBar = findViewById(R.id.progressBarDeteil)
        scrollView = findViewById(R.id.scrollView4)
        pic2 = findViewById(R.id.picDeteil)
        titleTxt = findViewById(R.id.MovieNameTxt)
        movieRateTxt = findViewById(R.id.movieStar)
        movieTimeTxt = findViewById(R.id.movieTime)
        movieSummaryInfo = findViewById(R.id.movieSummery)
        movieActorsInfo = findViewById(R.id.MovieActorInfo)
        favoriteIcon = findViewById(R.id.favoriteIcon5)


        favoriteIcon?.setOnClickListener {
            toggleFavorite()
        }
    }



    private fun sendRequest() {
        // Создание RequestQueue для Volley
        mRequestQueue = Volley.newRequestQueue(this)

        // Показать прогресс-бар и скрыть ScrollView
        progressBar!!.visibility = View.VISIBLE
        scrollView!!.visibility = View.GONE

        // Выполнение GET-запроса с использованием StringRequest
        val url = "https://moviesapi.ir/api/v1/movies/$idFilm"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Преобразуем JSON-ответ в объект FilmItem с помощью Gson
                val gson = Gson()
                val item: FilmItem = gson.fromJson(response, FilmItem::class.java)

                // Обновляем UI с полученными данными
                progressBar!!.visibility = View.GONE
                scrollView!!.visibility = View.VISIBLE

                // Загружаем изображение с помощью Glide
                Glide.with(this@DetailActivity)
                    .load(item.poster) // Доступ к свойству объекта
                    .into(pic2!!) // Устанавливаем картинку в ImageView

                // Обновляем остальные элементы UI
                titleTxt!!.text = item.title
                movieRateTxt!!.text = item.imdbRating
                movieTimeTxt!!.text = item.runtime
                movieSummaryInfo!!.text = item.plot
                movieActorsInfo!!.text = item.actors
            },
            { error ->
                progressBar!!.visibility = View.GONE
                scrollView!!.visibility = View.VISIBLE
                movieSummaryInfo!!.text = "Error fetching movie details."
            })

        // Добавляем запрос в очередь
        mRequestQueue!!.add(stringRequest)
    }

    private fun toggleFavorite() {
        // Создаем объект FilmItem на основе текущих данных
        val currentFilm = FilmItem(
            id = idFilm,
            title = titleTxt!!.text.toString(),
            imdbRating = movieRateTxt!!.text.toString(),
            runtime = movieTimeTxt!!.text.toString(),
            plot = movieSummaryInfo!!.text.toString(),
            actors = movieActorsInfo!!.text.toString(),
            poster = pic2?.tag?.toString() ?: "" // Сохраняем путь к изображению, если доступно
        )

        if (favoriteMovies.contains(currentFilm)) {
            // Если фильм уже в избранном, удаляем его
            favoriteMovies.remove(currentFilm)
            favoriteIcon!!.setColorFilter(null) // Сбрасываем цвет
            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
        } else {
            // Если фильма нет в избранном, добавляем
            favoriteMovies.add(currentFilm)
            favoriteIcon!!.setColorFilter(Color.RED)
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
        }

        // Переходим в Activity для избранного
        val intent = Intent(this, FavoriteActivity::class.java)
        intent.putParcelableArrayListExtra("favorites", ArrayList(favoriteMovies)) // Передаем список
        startActivity(intent)
    }


}

