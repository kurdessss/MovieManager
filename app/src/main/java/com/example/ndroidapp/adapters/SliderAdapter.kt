package com.example.ndroidapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ndroidapp.Domain.SliderItems
import com.example.ndroidapp.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class SliderAdapter(
    private val sliderItems: MutableList<SliderItems>, // MutableList для добавления элементов
    private val viewPager2: androidx.viewpager2.widget.ViewPager2 // Для бесконечного скролла
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private lateinit var context: Context // Контекст для Glide

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(sliderItem: SliderItems) {
            // Применение трансформации: обрезка и закругленные углы
            val requestOptions = RequestOptions()
                .centerCrop()
                .transform(RoundedCornersTransformation(60, 0)) // Угол закругления 60

            Glide.with(context)
                .load(sliderItem.imageResId) // Используем ресурс изображения
                .apply(requestOptions)
                .into(imageView) // Загружаем в ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context // Инициализация контекста
        val view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])

        // Если дошли до предпоследнего элемента, добавляем дубликаты для бесконечного скролла
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size

    // Runnable для добавления дубликатов в конец списка
    private val runnable = Runnable {
        sliderItems.addAll(sliderItems) // Дублируем элементы
        notifyDataSetChanged() // Обновляем адаптер
    }
}
