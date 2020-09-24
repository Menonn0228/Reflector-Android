package com.example.reflector_android.Models

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.R
import com.example.reflector_android.network.Article
import kotlinx.android.synthetic.main.article_item.view.*

class BlogRecyclerAdapter(val articles: MutableList<Article>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.article_item, parent, false)

        return CustomViewHolder(cellForRow)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val article = articles?.get(position)
        holder?.itemView.textView_Article.text = article?.description
        holder?.itemView.Title.text = article?.title
        holder?.itemView.Author.text = article?.author
        holder?.itemView.pubDate.text = article?.pubDate.toString()
//        var day = article?.pubDate?.dayOfMonth.toString()
//        var year = article?.pubDate?.year.toString()
//        var month = article?.pubDate?.monthValue.toString()
//        holder?.itemView.pubDate.text = month + "/" + day + "/" + year
    }

    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
}