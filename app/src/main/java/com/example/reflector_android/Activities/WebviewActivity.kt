package com.example.reflector_android.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reflector_android.R
import com.example.reflector_android.ViewHolders.ArticleListViewHolder
import kotlinx.android.synthetic.main.article_webview.*

class WebviewActivity(): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_webview)
        val url = intent.getStringExtra(ArticleListViewHolder.articleUrl)
        articleWebview.loadUrl(url)
    }
}