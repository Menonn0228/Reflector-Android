package com.example.reflector_android.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.R
import com.example.reflector_android.ViewHolders.CategoryListViewHolder
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryRecyclerAdapter : RecyclerView.Adapter<CategoryListViewHolder>() {
    var covid = getEmoji(0x1F637) + " COVID-19"
    var life = getEmoji(0x1F942) + " Life and Entertainment"
    var news = getEmoji(0x1F30E) + " News"
    var opinion = getEmoji(0x1F4AC) + " Opinion"
    var sports = getEmoji(0x1F3C8) + " Sports"

    var categories = arrayOf( covid , life , news , opinion , sports)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CategoryListViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.customView.categoryName.text = currentCategory
    }

    fun getEmoji(unicode : Int): String {
        return String(Character.toChars(unicode))
    }
}