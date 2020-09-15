package com.example.reflector_android.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.R
import com.example.reflector_android.network.Article
import kotlinx.android.synthetic.main.article_item.view.*

class BlogRecyclerAdapter(val articles: MutableList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.article_item, parent, false)

        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val article = articles.get(position)
        holder?.itemView.textView_Article.text = article.description
        holder?.itemView.Title.text = article.title
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class CustomViewHolder(v: View): RecyclerView.ViewHolder(v){

    }
}