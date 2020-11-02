package com.example.reflector_android.viewHolders

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.activities.WebviewActivity
import com.example.reflector_android.network.Article

class ArticleListViewHolder(val customView: View, var url: Article? = null) : RecyclerView.ViewHolder(customView) {
    companion object {
        const val articleUrl = "articleUrl"
    }

    init {
        customView.setOnClickListener {
            val intent = Intent(customView.context, WebviewActivity::class.java)
            intent.putExtra(articleUrl, url?.link)
            customView.context.startActivity(intent)
        }
    }
}