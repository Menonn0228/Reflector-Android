package com.example.reflector_android.Models

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.R
import com.example.reflector_android.network.Article
import kotlinx.android.synthetic.main.article_item.view.*

class BlogRecyclerAdapter(val articles: MutableList<Article>?) : RecyclerView.Adapter<CustomViewHolder>() {
    companion object {
        var isLoading: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.article_item, parent, false)

        return CustomViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
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

class CustomViewHolder(val customView: View, var url: Article? = null) : RecyclerView.ViewHolder(customView) {
        companion object {
            const val articleUrl = "articleUrl"
        }

        init {
            customView.setOnClickListener {
                println("you clicked the article")
                val intent = Intent(customView.context, WebviewActivity::class.java)
                intent.putExtra(articleUrl, url?.link)
                customView.context.startActivity(intent)
            }
        }
    }
