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
    companion object {
        var isLoading: Boolean = false
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.article_item, parent, false)

        return CustomViewHolder(cellForRow)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val article = articles?.get(position)

        when (article?.author) {
            null -> {
                holder?.itemView.progressBar.visibility = View.VISIBLE
                holder?.itemView.textView_Article.text = ""
                holder?.itemView.Title.text = ""
                holder?.itemView.Author.text = ""
                holder?.itemView.pubDate.text = ""
            }
            else -> {
                holder?.itemView.progressBar.visibility = View.GONE
                holder?.itemView.textView_Article.text = article?.description
                holder?.itemView.Title.text = article?.title
                holder?.itemView.Author.text = article?.author
                holder?.itemView.pubDate.text = article?.pubDate.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
}