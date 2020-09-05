package com.example.reflector_android
import com.example.reflector_android.network.Article
import okhttp3.*
import java.io.IOException

class RSSService {
    suspend fun fetchNews(): MutableList<Article> {
        val url = "http://reflector-online.com/search/?f=rss&t=article&l=5&c=news/*"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var test = XmlParser()
        var articles = mutableListOf<Article>()

//        client.newCall(request).enqueue(object: Callback {
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body!!.byteStream()
//                GlobalScope.launch {
//                    articles = test.parse(body)
//                }
//
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                println(e)
//            }
//        })
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val body = response.body!!.byteStream()
            articles = test.parse(body)
        }
        return articles
    }

}