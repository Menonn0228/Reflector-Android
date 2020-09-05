package com.example.reflector_android
import okhttp3.*
import java.io.IOException

class RSSService {
    // Make Changes

    fun fetchNews() {
        val url = "http://reflector-online.com/search/?f=rss&t=article&l=50&c=news/*"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }
        })
    }
}