package com.example.reflector_android.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Models.Category
import com.example.reflector_android.R
import com.example.reflector_android.ViewHolders.CategoryListViewHolder
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryRecyclerAdapter : RecyclerView.Adapter<CategoryListViewHolder>() {
    var covid = Category(" COVID-19", "covid-19" , getEmoji(0x1F637))
    var life = Category(" Life and Entertainment" , "life" , getEmoji(0x1F942))
    var news = Category(" News" , "news" , getEmoji(0x1F30E))
    var opinion = Category(  " Opinion" , "opinion" , getEmoji(0x1F4AC))
    var sports = Category(" Sports" , "sports" , getEmoji(0x1F3C8))

    var categories = arrayOf( covid , life , news , opinion , sports )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CategoryListViewHolder(cellForRow , "")
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val currentCategory = categories[position]
        var text = currentCategory.emoji + currentCategory.name
        holder.customView.categoryName.text = text
        holder.categoryName = currentCategory.rssVal
    }

    private fun getEmoji(unicode : Int): String {
        return String(Character.toChars(unicode))
    }
}