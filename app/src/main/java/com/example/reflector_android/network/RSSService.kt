package com.example.reflector_android

import com.example.reflector_android.network.Article
import okhttp3.*
import java.io.IOException

class RSSService {
    suspend fun fetchNews(): MutableList<Article>? {
        val url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20&c=news/*"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var test = XmlParser()
        var articles = mutableListOf<Article>()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            if (response.body == null) {
                return null
            }
            val body = response.body!!.byteStream()
            articles = test.parse(body)
        }
        return articles
    }

    suspend fun fetchMoreNews(offset: Int): MutableList<Article>? {
        val url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20&o=$offset&c=news/*"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var test = XmlParser()
        var articles = mutableListOf<Article>()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            if (response.body == null) {
                return null
            }
            val body = response.body!!.byteStream()
            articles = test.parse(body)
        }
        return articles
    }

}