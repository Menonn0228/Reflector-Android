 package com.example.reflector_android.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Activities.WebviewActivity
import com.example.reflector_android.R
import com.example.reflector_android.ViewHolders.ArticleListViewHolder
import com.example.reflector_android.network.Article
import kotlinx.android.synthetic.main.article_item.view.*

class BlogRecyclerAdapter(val articles: MutableList<Article>?) : RecyclerView.Adapter<ArticleListViewHolder>() {
    companion object {
        var isLoading: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.article_item, parent, false)

        return ArticleListViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        val article = articles?.get(position)

        when (article?.author) {
            null -> {
                holder.customView.progressBar.visibility = View.VISIBLE
                holder.customView.description.text = ""
                holder.customView.title.text = ""
                holder.customView.author.text = ""
                holder.customView.pubDate.text = ""
            }
            else -> {
                holder.customView.progressBar.visibility = View.GONE
                holder.customView.description.text = article.description
                holder.customView.title.text = article.title
                holder.customView.author.text = article.author
                holder.customView.pubDate.text = article.pubDate.toString()
                holder.url = article
            }
        }
    }
}


