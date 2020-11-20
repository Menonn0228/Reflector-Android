package com.example.reflector_android.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Activities.MainActivity
import com.example.reflector_android.Models.Category
import com.example.reflector_android.Models.CategoryIdentifier
import com.example.reflector_android.R
import com.example.reflector_android.ViewHolders.CategoryListViewHolder
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryRecyclerAdapter : RecyclerView.Adapter<CategoryListViewHolder>() {
    var covid = Category(CategoryIdentifier.covid19.title, CategoryIdentifier.covid19 , CategoryIdentifier.covid19.emoji)
    var life = Category(CategoryIdentifier.life.title , CategoryIdentifier.life , CategoryIdentifier.life.emoji)
    var news = Category(CategoryIdentifier.news.title , CategoryIdentifier.news , CategoryIdentifier.news.emoji)
    var opinion = Category(  CategoryIdentifier.opinion.title , CategoryIdentifier.opinion , CategoryIdentifier.opinion.emoji)
    var sports = Category(CategoryIdentifier.sports.title , CategoryIdentifier.sports , CategoryIdentifier.sports.emoji)

    var categories = arrayOf( covid , life , news , opinion , sports )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CategoryListViewHolder(cellForRow , null)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val currentCategory = categories[position]
        val text = currentCategory.emoji + currentCategory.name
        holder.customView.categoryName.text = text
        holder.categoryName = currentCategory.rssVal
    }


}