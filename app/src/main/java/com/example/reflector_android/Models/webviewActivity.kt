package com.example.reflector_android.Models

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reflector_android.R
import kotlinx.android.synthetic.main.article_webview.*

class webviewActivity(): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleWebview.settings.javaScriptEnabled

        setContentView(R.layout.article_webview)
        val url = intent.getStringExtra(CustomViewHolder.articleUrl)
        articleWebview.loadUrl(url)
    }




}