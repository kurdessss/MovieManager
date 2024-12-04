package com.example.ndroidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ndroidapp.Domain.Category
import com.example.ndroidapp.R

class CategoryListAdapter(
    private var categories: List<Category>, // Изменили на var, чтобы обновлять список
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    // Внутренний ViewHolder класс для элементов категории
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView = itemView.findViewById(R.id.TitleTxt)

        fun bind(category: Category) {
            categoryNameTextView.text = category.name
            itemView.setOnClickListener { onCategoryClick(category) }
        }
    }

    // Создание ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    // Возвращаем количество элементов
    override fun getItemCount(): Int = categories.size

    // Метод для обновления списка категорий
    fun submitList(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged() // Уведомляем адаптер о том, что данные изменились
    }
}