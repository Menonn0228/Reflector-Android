package com.example.reflector_android

import com.example.reflector_android.Models.CategoryIdentifier
import com.example.reflector_android.network.Article
import okhttp3.*
import java.io.IOException

class RSSService {
    suspend fun fetchNews(): MutableList<Article>? {
        val url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val test = XmlParser()
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

    suspend fun fetchNewsbyCategory(category: CategoryIdentifier): MutableList<Article>? {
        val tag = category.tag
        val url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20&c=$tag"
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

    suspend fun fetchMoreNews(offset: Int, category: String?): MutableList<Article>? {
        var url: String
        if (category == null) {
            url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20&o=$offset"
        }
        else {
            url = "http://reflector-online.com/search/?f=rss&t=article&s=start_time&sd=desc&l=20&o=$offset&c=$category/*"
        }
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